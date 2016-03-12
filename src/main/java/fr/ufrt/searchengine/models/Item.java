package fr.ufrt.searchengine.models;

import org.apache.solr.client.solrj.beans.Field;

public class Item 
{
    @Field("id")
    String id;
    @Field("score")
    Float score;

    public Item() {} // Empty constructor is required
    
    public Item(String id, Float score)
    {
        super();
        this.id = id;
        this.score=score;
    }
    
    // Getter Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Float getScore() {
        return score;
    }
    public void setScore(Float score) {
        this.score = score;
    }

}