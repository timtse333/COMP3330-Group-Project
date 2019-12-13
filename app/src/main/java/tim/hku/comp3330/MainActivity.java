package tim.hku.comp3330;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import tim.hku.comp3330.DataClass.User;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private AppBarConfiguration mAppBarConfigurationBeforeLogin;
    private DatabaseReference userRef;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean logined = prefs.getBoolean("IsLogin",false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        setSupportActionBar(toolbar);DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_edit_profile, R.id.nav_myprojects, R.id.nav_message, R.id.nav_login, R.id.nav_registration,R.id.nav_project_test,R.id.nav_logout, R.id.nav_post_blog, R.id.nav_post_progress)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if(logined){
            View headerView = navigationView.getHeaderView(0);
            CircleImageView image =  (CircleImageView)headerView.findViewById(R.id.imageView);
            TextView username = (TextView) headerView.findViewById(R.id.username);
            String userID = prefs.getString("userID","");
            userRef.orderByChild("userID").equalTo(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                        user = postSnapShot.getValue(User.class);
                    }
                    if (user.getIcon() != null) {
                        Picasso.with(getApplicationContext())
                                .load(user.getIcon())
                                .fit()
                                .centerCrop()
                                .into(image);
                        username.setText(user.getUserName());
                    }
                    else {
                        int img = getResources().getIdentifier("test","drawable","tim.hku.comp3330");
                        image.setImageResource(img);
                        username.setText(user.getUserName());
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            MenuItem accountMenu = navigationView.getMenu().findItem(R.id.AccountMenu);
            accountMenu.getSubMenu().findItem(R.id.nav_login).setVisible(false);
            accountMenu.getSubMenu().findItem(R.id.nav_login).setEnabled(false);
            accountMenu.getSubMenu().findItem(R.id.nav_registration).setVisible(false);
            accountMenu.getSubMenu().findItem(R.id.nav_registration).setEnabled(false);
            accountMenu.getSubMenu().findItem(R.id.nav_logout).setVisible(true);
            accountMenu.getSubMenu().findItem(R.id.nav_logout).setEnabled(true);
            accountMenu.getSubMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("IsLogin",false);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getApplicationContext(), projectCreation.class);
//                    startActivity(intent);
                    navController.navigate(R.id.nav_project_create );
                }
            });
        }
        else{
            fab.setVisibility(View.GONE);
            fab.setEnabled(false);
            drawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController nav = Navigation.findNavController(v);
                    nav.navigate(R.id.nav_login );
                }
            });
            MenuItem functionMenu = navigationView.getMenu().findItem(R.id.FunctionsMenu);
            functionMenu.setVisible(false);
            functionMenu.setEnabled(false);
            MenuItem accountMenu = navigationView.getMenu().findItem(R.id.AccountMenu);
            accountMenu.getSubMenu().findItem(R.id.nav_logout).setVisible(false);
            accountMenu.getSubMenu().findItem(R.id.nav_logout).setEnabled(false);
            navController.navigate(R.id.nav_login);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
