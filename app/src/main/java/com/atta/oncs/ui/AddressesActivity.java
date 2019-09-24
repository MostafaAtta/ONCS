package com.atta.oncs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.oncs.R;
import com.atta.oncs.contracts.AddressesContract;
import com.atta.oncs.model.Address;
import com.atta.oncs.model.AddressesAdapter;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.AddressesPresenter;

import java.util.ArrayList;

public class AddressesActivity extends AppCompatActivity implements AddressesContract.View{

    AddressesPresenter addressesPresenter;

    RecyclerView recyclerView;

    SessionManager sessionManager;

    TextView infoTextView;

    AddressesAdapter myAdapter;

    RelativeLayout addressesLayout;

    LinearLayout textLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);


        recyclerView = findViewById(R.id.my_add_recycler);
        addressesLayout = findViewById(R.id.relativeLayout);
        textLayout = findViewById(R.id.my_addresses_info);

        infoTextView = findViewById(R.id.my_addresses_info_tv);


        addressesPresenter = new AddressesPresenter(this, this);

        sessionManager = new SessionManager(this);

        addressesPresenter.getAddresses(sessionManager.getUserId());

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.my_add_refresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                () -> {
                    addressesPresenter.getAddresses(sessionManager.getUserId());
                    mySwipeRefreshLayout.setRefreshing(false);
                }
        );


        String mystring = getResources().getString(R.string.my_addresses_info_txt);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        infoTextView.setText(content);

        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewAddress();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        addNewAddress();
        if (id == R.id.add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Address> addresses) {

        recyclerView.setVisibility(View.VISIBLE);
        textLayout.setVisibility(View.GONE);
        myAdapter = new AddressesAdapter(this, addresses, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
    }
/*

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DishesLinearAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar

            AtomicBoolean undo = new AtomicBoolean(false);

            List<Address> addresses = myAdapter.getList();

            String name = addresses.get(viewHolder.getAdapterPosition()).getAddressName();

            // backup of removed item for undo purpose
            final Address deletedItem = addresses.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            myAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(addressesLayout, name + " removed from address!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {

                undo.set(true);
                // undo is selected, restore the deleted item
                myAdapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (!undo.get()){
                        addressesPresenter.removeAddresses(deletedItem.getId());
                    }

                }

                @Override
                public void onShown(Snackbar snackbar) {
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
*/

    @Override
    public void updateText() {

        recyclerView.setVisibility(View.GONE);
        textLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addNewAddress() {

        Intent intent = new Intent(AddressesActivity.this, NewAddressActivity.class);
        startActivity(intent);
    }
}
