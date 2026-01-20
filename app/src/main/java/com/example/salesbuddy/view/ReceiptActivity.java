package com.example.salesbuddy.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ItemsSale;
import com.example.salesbuddy.view.adapter.ResumeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends IncludeToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvItems = findViewById(R.id.rvItensVenda);

        configToolbar("COMPROVANTE");

        List<ItemsSale> itemsSale = new ArrayList<>();

        itemsSale.add(new ItemsSale("3,50", "Bolacha Maria"));
        itemsSale.add(new ItemsSale("10,50", "Calabresa Perdigão bem salgadinha"));
        itemsSale.add(new ItemsSale("29,50", "Computador"));
        itemsSale.add(new ItemsSale("6,70", "Cabeça de bode"));

        ResumeAdapter adapter = new ResumeAdapter(itemsSale, R.color.txInputSale);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);
    }
}