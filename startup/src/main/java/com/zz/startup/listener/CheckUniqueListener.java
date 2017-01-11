package com.zz.startup.listener;

import com.mongodb.DBObject;
import com.zz.startup.annotation.Unique;
import com.zz.startup.exception.CustomException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springside.modules.utils.Reflections;

import java.lang.reflect.Field;
import java.util.List;

public class CheckUniqueListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeSave(Object source, DBObject dbo) {
        Field[] fields = FieldUtils.getAllFields(source.getClass());
        for (Field field : fields) {
            Unique unique = field.getAnnotation(Unique.class);
            if (unique != null && isRepeat(source, field)) {
                throw new CustomException(unique.message());
            }
        }
    }

    private boolean isRepeat(Object source, Field field) {
        String fieldName = field.getName();
        Criteria criteria = Criteria.where(fieldName);
        criteria.is(Reflections.getFieldValue(source, fieldName));
        Criteria idCriteria = Criteria.where("_id");
        idCriteria.ne(Reflections.getFieldValue(source, "id"));

        Query query = new Query();
        query.addCriteria(criteria);
        query.addCriteria(idCriteria);
        List<?> list = mongoTemplate.find(query, source.getClass());
        return list.size() > 0;
    }
}
