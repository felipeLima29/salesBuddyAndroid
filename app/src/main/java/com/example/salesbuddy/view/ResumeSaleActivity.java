package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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

public class ResumeSaleActivity extends IncludeToolbar {
    private AppCompatButton btnFinishSale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnFinishSale = findViewById(R.id.btnFinishSale);

        btnFinishSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResumeSaleActivity.this, ReceiptActivity.class);
                startActivity(intent);
            }
        });

        configToolbar("RESUMO VENDA");
        List<ItemsSale> itemsSale = new ArrayList<>();

        itemsSale.add(new ItemsSale("3,50", "Bolacha Maria"));
        itemsSale.add(new ItemsSale("10,50", "Calabresa Perdigão bem salgadinha"));
        itemsSale.add(new ItemsSale("29,50", "Computador"));
        itemsSale.add(new ItemsSale("6,70", "Cabeça de bode"));

        RecyclerView rvItems = findViewById(R.id.rvItensVenda);
        ResumeAdapter adapter = new ResumeAdapter(itemsSale, R.color.blue);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

    }
}