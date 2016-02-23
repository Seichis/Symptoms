package com.masterthesis.personaldata.symptoms.dragNdrop;

import com.masterthesis.personaldata.symptoms.DAO.model.Diary;

/**
 * Created by Konstantinos Michail on 2/21/2016.
 */
public class DiaryItem extends Item {

    Diary diary;

    public DiaryItem(int icon, int spans, String title, String description) {
        super(icon, spans, title, description);
    }
}
