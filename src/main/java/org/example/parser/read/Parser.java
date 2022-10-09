package org.example.parser.read;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Parser {
    public static Document getDocument() throws IOException {
//        String url = "https://ekom.uz";Главная страница
        String url="https://ekom.uz/setevoe-oborudovanie/aktivnoe-oborudovanie/servera/";
        Document document = Jsoup.parse(new URL(url), 4000);
//        System.out.println(document);
        return document;
    }

    public static void main(String[] args) throws IOException {
//        Document document = getDocument();
//        Element body = document.body();
////        System.out.println(body);
//        Elements catalogs = body.select("ul[class=ty-menu__items cm-responsive-menu]");//сами ссылки на котологи
//        for (Element catalog : catalogs.select("li[class=ty-menu__item cm-menu-item-responsive first-lvl]")) {
//            Element nameCatalog = catalog.select("span[class=menu-lvl-ctn exp-wrap]").first();
//            System.out.println(nameCatalog.select("bdi").text());//название католога
//            Elements inputCatalog = catalog.select("div[class=ty-menu__submenu-col]");//название блока
//            for (Element blockInputCatolog : inputCatalog.select("div[class=second-lvl]")) {
////                System.out.println(blockInputCatolog);
//                System.out.print("\t\t");
//                System.out.println(blockInputCatolog.select("div.ty-menu__submenu-item-header").select("bdi").text());
//                Elements hrefItems = blockInputCatolog.select("div.ty-menu__submenu-item");
//                for (Element hrefItem : hrefItems) {
//                    System.out.println("\t\t\t\t" + hrefItem.select("a").select("span").text());
//                    System.out.println("\t\t\t\t"+hrefItem.select("a").attr("href"));
//                }
//            }
//        }



        Document document=getDocument();
        Element body=document.body();
        Element itemsBlock=body.select("div.grid-list").first();
        Element items=itemsBlock.getElementById("categories_view_pagination_contents");
        for(Element item:items.select("div.ty-column3")){
            //photo
            //там есть две фотки разных размеров
            System.out.println(item.select("div.ut2-gl__image").select("img.ty-pict").attr("data-src"));
            
        }
//        System.out.println(items);
    }
}
