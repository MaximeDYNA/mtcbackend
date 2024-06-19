package com.catis.objectTemporaire;

import java.util.List;
import java.util.UUID;
import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Document(indexName = "visite_index")
public class VisiteSearchService {
    @Id
    @Field(type = FieldType.Text, name = "id")
    private UUID id;

    @Field(type = FieldType.Integer, name="is_conform")
    private int isConform;

    @Field(type = FieldType.Integer, name="is_conform")
    private int statutVisite;

    @Field(type = FieldType.Text, name="organisation")
    private String organisation;

    @Field(type = FieldType.Boolean, name = "contre_visite")
    @JsonSerialize(using = ActiveStatusSerializer.class)
    private boolean contreVisite;




    // @Field(type = FieldType.Boolean)
    // private boolean conformityTest;


    @Field(type = FieldType.Text)
    private String bestPlate;

    @Field(type = FieldType.Double)
    private double accurance;

    // @Field(type = FieldType.Date)
    // private Date date;

    @Field(type = FieldType.Boolean)
    private boolean newCss;

    @Field(type = FieldType.Text)
    private String document;


    @Field(type = FieldType.Nested)
    private List<Links> links;


    public static class Links {
        @Field(type = FieldType.Keyword)
        private String link;
    }



}
