package com.luv2code.springboot.demo.mycoolapp.rest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;


public class FunRestModel1 {
	
	String ROOT_URL;
	public static final String APPLICATION_UA = "BuildItWebcrawler";

	boolean junit=false;
	String response="";
    public static final List<String> fileRegEx = new ArrayList<>();
    public static final String noPageRegEx = ".*?\\#.*";
    private Map<String, Integer> internalLinks = new TreeMap<>();
    private Map<String, Integer> externalLinks = new TreeMap<>();
    private Map<String, Integer> imageLinks = new TreeMap<>();
    private int pagesCrawled = 1;


    /**
     * Get the Internal-Links-Map / -List
     * @return Internal-Links-Map
     */
    public Map<String, Integer> getInternalLinksList() {
        return internalLinks;
    }


    /**
     * Get the Internal-Links-Map / -List
     * @return Internal-Links-Map
     */
    public Map<String, Integer> getExternalLinksList() {
        return externalLinks;
    }
    /**
     * Check whether a specific list contains this url already or not
     * @param list list/map which may contain the link
     * @param url  url to add
     * @return list contains url or not
     */
    private boolean listContains(Map<String, Integer> list, String url) {
        return list.keySet().contains(url);
    }


	
	public String testFunRestModel(String root_url, boolean junit)	{		this.junit=junit;

		String url="http://ibm.com";

		this.ROOT_URL=root_url;
		//	when junit==true data is collected in Junit designated data structures
		
        /**
         * Add some File-RegExs
         */
        fileRegEx.add(".*?\\.png");
        fileRegEx.add(".*?\\.jpg");
        fileRegEx.add(".*?\\.jpeg");
        fileRegEx.add(".*?\\.gif");
        fileRegEx.add(".*?\\.zip");
        fileRegEx.add(".*?\\.7z");
        fileRegEx.add(".*?\\.rar");
        fileRegEx.add(".*?\\.css.*");
        fileRegEx.add(".*?\\.js.*");

		url=ROOT_URL;
		crawlPage(url);
        
        System.out.println(internalLinks.size());
        response=response+"Image Links:"+"<br>";
        for (Map.Entry<String,Integer> entry : imageLinks.entrySet())  
        response=response+entry.getKey()+"<br>"; 

        response=response+"Internal Links:"+"<br>";
        for (Map.Entry<String,Integer> entry : internalLinks.entrySet())  
        response=response+entry.getKey()+"<br>"; 

        response=response+"External Links:"+"<br>";
        for (Map.Entry<String,Integer> entry : externalLinks.entrySet())  
        response=response+entry.getKey()+"<br>"; 

        
		return response;
	}
        /**
         * Recursive page-crawler
         * @param url url to crawl
         */
        private void crawlPage(String url) {
            addPage(0, url);

            for (int i = 0; i < String.valueOf(pagesCrawled - 1).length() + 10; i++) {
                System.out.print("\b");
            }
            System.out.print("CRAWLING #" + pagesCrawled);
            pagesCrawled++;

        
        Document doc = new Document("");
        try {
            doc = Jsoup.connect(url).userAgent(APPLICATION_UA).timeout(5000).get();
        } catch (IOException e) {
            System.out.println("\nUnable to read Page at [" + url + "]: " + e.getMessage());
            addPage(0, url, 500);
            return;
        }

        // Get every image from that page
        Elements images = doc.select("img");
        for (Element image : images) {
            String imageUrl = image.attr("abs:src");
            // It is an image --> add to list
            if (!listContains(imageLinks, imageUrl) && !imageUrl.equals("")) addPage(2, imageUrl);
        }

        // Get every link from that page
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String linkUrl = link.attr("abs:href");
            if (linkUrl.startsWith(ROOT_URL)) { // Found an internal link
                if (!linkUrl.matches(noPageRegEx)) { // This link does not end with a "#"
                									// do nothing if it ends with a "#"
                    // Is link a file? A loop over fileRegEx list 
                    for (String regex : fileRegEx) { // A list of url endings that means a file
                        if (linkUrl.matches(regex)) {
                            // It is a file --> add to list, but do not try to crawl
                            addPage(0, linkUrl);
                            return;	//from method crawlPage					
                        }
                    }

                    // No file --> crawl this page. recursine call in case of internal link.
                    if (!listContains(internalLinks, linkUrl) && !linkUrl.equals("")) crawlPage(linkUrl);
                }
            } else { // Found an external link. (linkUrl.startsWith(ROOT_URL))==false
                if (!listContains(externalLinks, linkUrl) && !linkUrl.equals("")) addPage(1, linkUrl);
            }
        }
        }      

	
	
    /**
     * Add a new page/link/url to the corresponding list
     * @param listId list/map which will contain the url
     * @param url    url to add
     */
    private void addPage(int listId, String url) {
    	
        int statuscode = new ConnectionTester(url).getStatuscode();
        if (listId == 0) {
            internalLinks.put(url, statuscode);
        } else if (listId == 1) {
            externalLinks.put(url, statuscode);
        } else if (listId == 2) {
            imageLinks.put(url, statuscode);
        }

    }

    /**
     * Add a new page/link/url to the corresponding list
     * @param listId     list/map which will contain the url
     * @param url        url to add
     * @param statuscode http-statuscode of this url
     */
    private void addPage(int listId, String url, int statuscode) {
        if (listId == 0) {
            internalLinks.put(url, statuscode);
        } else if (listId == 1) {
            externalLinks.put(url, statuscode);
        } else if (listId == 2) {
            imageLinks.put(url, statuscode);
        }
    }
	
	}
	
	

