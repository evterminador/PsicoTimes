package com.example.formandocodigo.psicotimes.data.entity.mapper;

import com.example.formandocodigo.psicotimes.data.entity.AppEntity;
import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public class AppEntityDataMapper {

    @Inject
    public AppEntityDataMapper() {}

    public App transform(AppEntity appEntity) {
        App app = null;
        if (appEntity != null) {
            app = new App();
            app.setId(appEntity.getId());
            app.setName(appEntity.getName());
            app.setRelevance(appEntity.getRelevance());
            app.setImage(appEntity.getImage());
            app.setDescription(appEntity.getDescription());
            try {
                app.setCreated_at(Converts.convertStringToTimestamp(appEntity.getCreated_at()));
                app.setUpdated_at(Converts.convertStringToTimestamp(appEntity.getUpdated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return app;
    }

    public ArrayList<App> transformArrayList(ArrayList<AppEntity> appEntities) {
        final ArrayList<App> appList = new ArrayList<>();
        for (AppEntity appEntity : appEntities) {
            final App app = transform(appEntity);
            if (app != null) {
                appList.add(app);
            }
        }
        return appList;
    }
}
