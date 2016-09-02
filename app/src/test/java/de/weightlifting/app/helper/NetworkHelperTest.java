package de.weightlifting.app.helper;

import junit.framework.Assert;

import org.junit.Test;

import de.weightlifting.app.news.News;
import de.weightlifting.app.news.NewsItem;

public class NetworkHelperTest {

    @Test
    public void testSendAuthenticatedHttpGetRequest() throws Exception {
        String result = "{\"result\": {\"publisher\": \"BVDG\", \"url\": \"http://www.german-weightlifting.de/passwort-regelung-fuer-adams-angepasst/\", \"image\": \"http://www.german-weightlifting.de/wp-content/uploads/2016/08/wp-1471390316731.png\", \"content\": \"Liebe BVDG-Kaderathleten,nnwir mu00f6chten Euch daru00fcber informieren, dass die WADAu00a0die Passwort-Regelung fu00fcr ADAMS angepasst hat. Alle Athletinnen und Athleten des RTP- und des NTP mu00fcssen daher ihr ADAMS-Passwort u00e4ndern. Das neue Passwort muss aus mindestens acht Zeichen bestehen, Sonderzeichen, mindestens einer Nummer und Grou00df- und Kleinbuchstaben enthalten. Die letzten zwu00f6lf Passwu00f6rter ku00f6nnen nicht mehr genutzt werden. Bei einigen Sportlerinnen und Sportler scheint es zudem derzeit zu Problemen bei der u00d6ffnung von ADAMS zu kommen. Hier hilft in der Regel die Lu00f6schung des Caches. Bei Problemen ku00f6nnen die Athletinnen und Athleten direkt die NADA kontaktieren (ed.ad1472663776an@sk1472663776d1472663776u00a0oderu00a00228 / 812 92-0).nnu00a0nnMit freundlichen Gru00fcu00dfennMichael VaternAnti-Doping-Beauftragter BVDG\", \"date\": \"1472169600.0\", \"heading\": \"Passwort-Regelung fu00fcr ADAMS angepasst\"}}";
        News news = new News();
        news.parseFromString(result);
        Assert.assertEquals(news.getItems().size(), 1);
        NewsItem newsItem = (NewsItem) news.getItem(0);
        Assert.assertEquals(newsItem.getPublisher(), "BVDG");
        Assert.assertEquals(newsItem.getURL(), "http://www.german-weightlifting.de/passwort-regelung-fuer-adams-angepasst/");
        Assert.assertEquals(newsItem.getImageURL(), "http://www.german-weightlifting.de/wp-content/uploads/2016/08/wp-1471390316731.png");
        Assert.assertEquals(newsItem.getHeading(), "Passwort-Regelung fu00fcr ADAMS angepasst");
        Assert.assertTrue(newsItem.getContent().startsWith("Liebe BVDG-Kaderathleten"));
        Assert.assertTrue(newsItem.getContent().endsWith("ing-Beauftragter BVDG"));
        Assert.assertEquals(newsItem.getDate(), "26.08.2016");
    }
//
//    @Test
//    public void testParseArticleFromUrl() throws Exception {
//        News news = new News();
//        news.addArticleFromUrl("http://weightliftinggermany.appspot.com/get_article?url=http://www.german-weightlifting.de/passwort-regelung-fuer-adams-angepasst/");
//        Thread.sleep(3000);
//        Assert.assertEquals(news.getItems().size(), 1);
//        NewsItem newsItem = (NewsItem) news.getItem(0);
//        Assert.assertEquals(newsItem.getPublisher(), "BVDG");
//        Assert.assertEquals(newsItem.getURL(), "http://www.german-weightlifting.de/passwort-regelung-fuer-adams-angepasst/");
//        Assert.assertEquals(newsItem.getImageURL(), "http://www.german-weightlifting.de/wp-content/uploads/2016/08/wp-1471390316731.png");
//        Assert.assertEquals(newsItem.getHeading(), "Passwort-Regelung fu00fcr ADAMS angepasst");
//        Assert.assertTrue(newsItem.getContent().startsWith("Liebe BVDG-Kaderathleten"));
//        Assert.assertTrue(newsItem.getContent().endsWith("ing-Beauftragter BVDG"));
//        Assert.assertEquals(newsItem.getDate(), "26.08.2016");
//    }
}