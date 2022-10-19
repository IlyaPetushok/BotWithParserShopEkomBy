package org.example.parser.read;

import org.example.parser.entity.Item;
import org.example.parser.entity.ServerAttribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class Parser {
    private static final String HOME_PAGE = "https://ekom.uz";

    public Document getDocument(String url) throws IOException {
        return Jsoup.parse(new URL(url), 4000);
    }

    public List<Item> itemsShow(String url, String numberPage) throws IOException {
        if (Integer.parseInt(numberPage) > 1) {
            url += "page-" + numberPage + "/";
        }
        Document document = getDocument(url);
        Element body = document.body();
        //read quantity page was here
        List<Item> itemsList = new ArrayList<>();
            Element itemsBlock = body.select("div.grid-list").first();
            if (itemsBlock != null) {
                Element items = itemsBlock.getElementById("categories_view_pagination_contents");
                for (Element item : items.select("div.ty-column3").select("div.ut2-gl__item ")) {
                    String photo = item.select("div.ut2-gl__image").select("img.ty-pict").attr("data-src");
                    String name = item.select("div.ut2-gl__name").text();
                    String status = item.select("div.ut2-gl__amount").text();
                    String price = item.select("div.ut2-gl__price").text();
                    itemsList.add(new Item(photo, name, status, price));
                }
                return itemsList;
            }
        return null;
    }

    public List<ServerAttribute> showCategory() throws IOException {
        List<ServerAttribute> serverAttributes = new ArrayList<>();
        List<String> nameDownCatalogList;
        Document document = getDocument(HOME_PAGE);
        Element body = document.body();
        for (Element catalog : getCatalog(body)) {
            String nameCatalog = catalog.select("span[class=menu-lvl-ctn exp-wrap]").select("bdi").text();
            nameDownCatalogList = new ArrayList<>();
            for (Element downCatalog : catalog.select("div.second-lvl")) {
                nameDownCatalogList.add(downCatalog.select("div.ty-menu__submenu-item-header").select("bdi").text());
            }
            serverAttributes.add(new ServerAttribute(nameCatalog, nameDownCatalogList));
        }
        return serverAttributes;
    }

    public List<ServerAttribute> getInputDownCatalog(String nameCatalog, String id) throws IOException {
        Document document = getDocument(HOME_PAGE);
        Element body = document.body();
        List<ServerAttribute> itemsList = new ArrayList<>();
        for (Element catalog : getCatalog(body)) {
            if (nameCatalog.equals(catalog.select("span[class=menu-lvl-ctn exp-wrap]").select("bdi").text())) {
                String nameDownCatalog = catalog.select("div.second-lvl").select("div.ty-menu__submenu-item-header").get(Integer.parseInt(id)).select("bdi").text();
                Element inputDownCatalogs = catalog.select("div.ty-menu__submenu-col").select("div.second-lvl").get(Integer.parseInt(id));
                for (Element item : getItems(inputDownCatalogs)) {
                    String typeItem = item.select("a").select("span").text();
                    String hrefItem = item.select("a").attr("href");
                    itemsList.add(new ServerAttribute(nameDownCatalog, typeItem, hrefItem));
                }
            }
        }
        return itemsList;
    }

    private Elements getCatalog(Element document) {
        Elements catalogs = document.select("ul[class=ty-menu__items cm-responsive-menu]");
        return catalogs.select("li[class=ty-menu__item cm-menu-item-responsive first-lvl]");
    }

    private Elements getItems(Element document) {
        return document.select("div.ty-menu__submenu-list").select("div.ty-menu__submenu-item");
    }

    public ServerAttribute getHrefItem(String nameCatalog, String nameDownCatalog, String name) throws IOException {
        Document document = getDocument(HOME_PAGE);
        Element body = document.body();
        String hrefItem = null, typeItem = null;
        for (Element catalog : getCatalog(body)) {
            if (nameCatalog.equals(catalog.select("span[class=menu-lvl-ctn exp-wrap]").select("bdi").text())) {
                for (Element downCatalog : catalog.select("div.second-lvl")) {
                    if (nameDownCatalog.equals(downCatalog.select("div.ty-menu__submenu-item-header").select("bdi").text())) {
                        for (Element item : getItems(downCatalog)) {
                            if (item.select("a").select("span").text().equals(name)) {
                                typeItem = item.select("a").select("span").text();
                                hrefItem = item.select("a").attr("href");
                            }
                        }
                    }
                }
            }
        }
        return new ServerAttribute(typeItem, hrefItem);
    }
}


//    String strQuantityPage;
//        if (body.select("div.ty-pagination__bottom").select("a.ty-pagination__range").size() > 0) {
//            quantityPage = body.select("div.ty-pagination__bottom").select("a.ty-pagination__range").first();
//            String[] arr = quantityPage.text().split("- ");
//            strQuantityPage = arr[arr.length - 1];
//        } else if (body.select("div.ty-pagination__bottom").select("a").size() > 0) {
//            Elements page = body.select("div.ty-pagination__bottom").select("a");
//            quantityPage = page
//                    .stream()
//                    .filter(s -> s == page.get(page.size() - 2))
//                    .findAny()
//                    .orElse(null);
//            strQuantityPage = quantityPage.text();
//        } else {
//            strQuantityPage = "0";
//        }