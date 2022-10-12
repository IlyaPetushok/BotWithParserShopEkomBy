package org.example.parser.read;

import org.example.parser.entity.ServerAttribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Parser {

    public Document getDocument(String url) throws IOException {
//        String url = "https://ekom.uz";Главная страница
//        String url = "https://ekom.uz/setevoe-oborudovanie/aktivnoe-oborudovanie/servera/";
//                "https://ekom.uz/setevoe-oborudovanie/aktivnoe-oborudovanie/servera/";
        //"https://ekom.uz/setevoe-oborudovanie";
        Document document = Jsoup.parse(new URL(url), 4000);
//        System.out.println(document);
        return document;
    }


    public void itemsShow(String url) throws IOException {
        Document document = getDocument(url);
        Element body = document.body();
        Element quantityPage;
        String strQuantityPage;
        if (body.select("div.ty-pagination__bottom").select("a.ty-pagination__range").size() > 0) {
            quantityPage = body.select("div.ty-pagination__bottom").select("a.ty-pagination__range").first();
            String[] arr = quantityPage.text().split("- ");
            strQuantityPage = arr[arr.length - 1];
        } else if (body.select("div.ty-pagination__bottom").select("a").size() > 0) {
//            quantityPage=body.select("div.ty-pagination__bottom").select("a");
            Elements page = body.select("div.ty-pagination__bottom").select("a");
            quantityPage = page
                    .stream()
                    .filter(s -> s == page.get(page.size() - 2))
                    .findAny()
                    .orElse(null);
            strQuantityPage = quantityPage.text();
        } else {
            strQuantityPage = "0";
        }
        System.out.println("quantity page:" + strQuantityPage);
        Element itemsBlock = body.select("div.grid-list").first();
        Element items = itemsBlock.getElementById("categories_view_pagination_contents");
        for (Element item : items.select("div.ty-column3").select("div.ut2-gl__item ")) {
            //photo
            //там есть две фотки разных размеров
            System.out.println("photo: " + item.select("div.ut2-gl__image").select("img.ty-pict").attr("data-src"));
            //name
            System.out.println("name: " + item.select("div.ut2-gl__name").text());
            //status(в наличии,нет в наличии ,предзаказ)
            System.out.println("status: " + item.select("div.ut2-gl__amount").text());
            //цена (цифры ,свяжитесь с нами для уточнения)
            System.out.println("price: " + item.select("div.ut2-gl__price").text());
        }

    }

    public List<ServerAttribute> showCategory() throws IOException {
        int i = 1;
        List<ServerAttribute> serverAttributes = new ArrayList<>();
        List<String> nameDownCatalogList = null;
        String newUrl;
        Scanner in = new Scanner(System.in);
        Document document = getDocument("https://ekom.uz");
        Element body = document.body();
        Elements catalogs = body.select("ul[class=ty-menu__items cm-responsive-menu]");//сами ссылки на котологи
//        System.out.println("Выберите каталог");
        for (Element catalog : catalogs.select("li[class=ty-menu__item cm-menu-item-responsive first-lvl]")) {
            String nameCatalog = catalog.select("span[class=menu-lvl-ctn exp-wrap]").select("bdi").text();
            System.out.println(i++ + ":" + nameCatalog);//название католога
            nameDownCatalogList = new ArrayList<>();
            for (Element downCatalog : catalog.select("div.second-lvl")) {
                nameDownCatalogList.add(downCatalog.select("div.ty-menu__submenu-item-header").select("bdi").text());
//                System.out.println("\t" + downCatalog.select("div.ty-menu__submenu-item-header").select("bdi").text());
            }
            serverAttributes.add(new ServerAttribute(nameCatalog, nameDownCatalogList));
        }
//        i = 1;
//        int indexCatalog = in.nextInt();
//        Elements inputCatalog = catalogs
//                .select("li[class=ty-menu__item cm-menu-item-responsive first-lvl]")
//                .get(indexCatalog - 1)
//                .select("div[class=ty-menu__submenu-col]");//название блока
//        for (Element blockInputCatalog : inputCatalog.select("div[class=second-lvl]")) {
//
//            System.out.print("\t\t");
//            System.out.println(i++ + ":" + blockInputCatalog.select("div.ty-menu__submenu-item-header").select("bdi").text());
//        }
//        i = 1;
//        int indexDownCatalog = in.nextInt();
//        Elements hrefItems = inputCatalog.select("div[class=second-lvl]").get(indexDownCatalog - 1).select("div.ty-menu__submenu-item");
//        for (Element hrefItem : hrefItems) {
//            System.out.println("\t\t\t\t" + i++ + " " + hrefItem.select("a").select("span").text());
//            System.out.println("\t\t\t\t" + hrefItem.select("a").attr("href"));
//        }
//        int indexHref = in.nextInt();
//        return hrefItems
//                .get(indexHref - 1)
//                .select("a")
//                .attr("href");
        return serverAttributes;
    }

    public List<ServerAttribute> getInputDownCatalog(String nameCatalog, String id) throws IOException {
        Document document = getDocument("https://ekom.uz");
        Element body = document.body();
        List<ServerAttribute> itemsList=new ArrayList<>();
        Elements catalogs = body.select("ul[class=ty-menu__items cm-responsive-menu]");
        for (Element catalog : catalogs.select("li[class=ty-menu__item cm-menu-item-responsive first-lvl]")) {
            if (nameCatalog.equals(catalog.select("span[class=menu-lvl-ctn exp-wrap]").select("bdi").text())) {
                String nameDownCatalog=catalog.select("div.second-lvl").select("div.ty-menu__submenu-item-header").get(Integer.parseInt(id)).select("bdi").text();
                Element inputDownCatalogs = catalog.select("div.ty-menu__submenu-col").select("div.second-lvl").get(Integer.parseInt(id));
                Elements items = inputDownCatalogs.select("div.ty-menu__submenu-list").select("div.ty-menu__submenu-item");
//                System.out.println(inputDownCatalogs.select("div.ty-menu__submenu-list").select("div.ty-menu__submenu-item"));
                for (Element item : items) {
                    String typeItem = item.select("a").select("span").text();
                    String hrefItem = item.select("a").attr("href");
                    itemsList.add(new ServerAttribute(nameDownCatalog,typeItem, hrefItem));
                }
            }
        }
        return itemsList;
    }
}
