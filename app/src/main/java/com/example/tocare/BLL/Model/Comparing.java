package com.example.tocare.BLL.Model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Comparing {

   public static class DateCompare implements Comparator<Task> {
        @Override
        public int compare(@NonNull Task o1, @NonNull Task o2) { SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date dateObject1 = null;
            try {
                dateObject1 = dateFormat.parse(o1.getCreated());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date dateObject2 = null;
            try {
                dateObject2 = dateFormat.parse(o2.getCreated());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert dateObject1 != null;
            return dateObject1.compareTo(dateObject2);

        }
    }
}
