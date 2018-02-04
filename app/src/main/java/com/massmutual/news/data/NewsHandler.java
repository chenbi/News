package com.massmutual.news.data;

import android.content.ContentValues;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;


import com.massmutual.news.data.NewsContract.Clip;


public class NewsHandler {

    private static final String TAG = "ArticleHandler";


    public static final class ArticleHandler extends FeedHandler {

        @Override
        public ArrayList<ContentValues> parse(InputStream in) {
            final ObjectMapper mapper = new ObjectMapper();

            try {
                ArrayList<ContentValues> cvs = new ArrayList<ContentValues>();
                JsonNode root = mapper.readTree(in).at("/results");

                Iterator<JsonNode> articles = root.elements();

                while (articles.hasNext()) {
                    JsonNode featured;
                    JsonNode image;

                    ContentValues cv = new ContentValues();


                        featured = articles.next();
                        cv.put(NewsContract.Clip.FEATURED_ID, featured.at("/" + Clip.KEY_FEATURED_ID).asText());
                        cv.put(NewsContract.Clip.FEATURED_TITLE, featured.at("/" + Clip.KEY_FEATURED_TITLE).asText());
                        cv.put(Clip.DESC, featured.at("/abstract").asText());
                        cv.put(Clip.FEATURED_DESC, featured.at("/abstract").asText());

                        cv.put(Clip.ORDER, featured.at("/" + Clip.KEY_ORDER).asInt());

                        cv.put(Clip.PUBLISHED_AT,featured.at("/published_date").asText().substring(0, 10) );

                        Iterator<JsonNode> images = featured.at("/multimedia").elements();
                        if (images.hasNext()) {

                            images.next();
                            image = images.next();

                            cv.put(Clip.IMAGE_URL, image.at("/url").asText());
                            images.next();
                            images.next();
                            image= images.next();
                            cv.put(Clip.THUMBNAIL, image.at("/url").asText());
                            cvs.add(cv);
                        }


                }

                return cvs;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}