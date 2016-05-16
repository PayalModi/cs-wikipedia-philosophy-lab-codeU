package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
    
    public static Element findLink(Element paragraph) {
        Iterable<Node> para = new WikiNodeIterable(paragraph);
        for (Node node: para) {
            if (node instanceof Element) {
                Element elem = (Element)node;
                if (isLink(elem))
                    return elem;
            }
        }
        return null;
    }
    
    public static boolean isLink(Element el) {
        return el.tagName().equals("a");
    }
    
    public static Element searchParas(String url) throws IOException {
        Elements paragraphs = wf.fetchWikipedia(url);
        
        for (Element para: paragraphs) {
            Element link = findLink(para);
            if (link != null)
                return link;
        }
        
        return null;
    }
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        
        for (int i = 0; i < 5; i++) {
            Element firstLink = searchParas(url);
            if (firstLink == null) {
                System.out.println("No links. End");
                return;
            }
            
            url = firstLink.attr("abs:href");
            
            if (url.equals("https://en.wikipedia.org/wiki/Philosophy")) {
                System.out.println("Reached philosophy");
                return;
            }
        }
        
        System.out.println("Not found");
    }
}
