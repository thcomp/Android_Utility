package jp.co.thcomp.android_utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static MenuItem[] sMenuItemArray = {
            new MenuItem("data manager", DataManagerTestActivity.class),
            new MenuItem("dialog helper", DialogHelperTestActivity.class),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (MenuItem menuItem : sMenuItemArray) {
            addMenu(menuItem);
        }
    }

    private void addMenu(final MenuItem item) {
        ViewGroup parent = (ViewGroup) findViewById(R.id.llMenuItemList);
        View view = getLayoutInflater().inflate(R.layout.listitem_menuitem, parent, false);
        ((TextView) view.findViewById(R.id.tvMenuTitle)).setText(item.menuTitle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, item.targetActivity);
                startActivity(intent);
            }
        });
        parent.addView(view);
    }

    private static class MenuItem {
        String menuTitle;
        Class targetActivity;

        public MenuItem(String title, Class activityClass) {
            menuTitle = title;
            targetActivity = activityClass;
        }
    }
}
