package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.dao.ProductArrivalDao;
import com.example.myapplication.dao.SaleProductDao;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ProductArrivalTable;
import com.example.myapplication.entity.SaleProductTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Report extends AppCompatActivity {
    private Date dateFrom, dateTo;
    private TextView tvSelectedDate1, tvSelectedDate2, tvReportResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Инициализация элементов интерфейса
        initViews();

        // Установка текущей даты по умолчанию
        setDefaultDates();
    }

    private void initViews() {
        tvSelectedDate1 = findViewById(R.id.tvSelectedDate1);
        tvSelectedDate2 = findViewById(R.id.tvSelectedDate2);
        tvReportResult = findViewById(R.id.tvReportResult);
        Button btnOpenDatePicker1 = findViewById(R.id.btnDatePicker1);
        Button btnOpenDatePicker2 = findViewById(R.id.btnDatePicker2);

        btnOpenDatePicker1.setOnClickListener(v -> showDatePicker(true));
        btnOpenDatePicker2.setOnClickListener(v -> showDatePicker(false));
    }

    private void setDefaultDates() {
        Calendar calendar = Calendar.getInstance();
        dateTo = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        dateFrom = calendar.getTime();

        updateDateTexts();
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(isStartDate ? dateFrom : dateTo);

        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);

                    if (isStartDate) {
                        dateFrom = selected.getTime();
                    } else {
                        dateTo = selected.getTime();
                    }
                    updateDateTexts();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateDateTexts() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvSelectedDate1.setText(sdf.format(dateFrom));
        tvSelectedDate2.setText(sdf.format(dateTo));
    }

    public void goBack(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void getReport(View v) {
        if (dateFrom == null || dateTo == null) {
            showToast("Выберите обе даты!");
            return;
        }

        if (dateFrom.after(dateTo)) {
            showToast("Дата 'с' должна быть раньше даты 'по'");
            return;
        }

        new Thread(() -> {
            try {
                // 1. Получаем данные из БД
                List<ProductArrivalTable> arrivals = MyApp.database.ProductArrivalDao().getArrivalsBetweenDates(
                        dateFrom.getTime(), dateTo.getTime());

                List<SaleProductTable> sales = MyApp.database.SaleProductDao().getSalesBetweenDates(
                        dateFrom.getTime(), dateTo.getTime());

                // 2. Формируем отчет
                String reportText = generateReportText(arrivals, sales);

                // 3. Отображаем результат
                runOnUiThread(() -> {
                    tvReportResult.setText(reportText);
                    showToast("Отчет сформирован");
                });

            } catch (Exception e) {
                Log.e("ReportError", "Ошибка при формировании отчета", e);
                runOnUiThread(() -> showToast("Ошибка: " + e.getMessage()));
            }
        }).start();
    }

    private String generateReportText(List<ProductArrivalTable> arrivals, List<SaleProductTable> sales) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        // Заголовок отчета
        sb.append("══════════════════════════════\n");
        sb.append(String.format("ОТЧЕТ ЗА ПЕРИОД\n%s - %s\n",
                sdf.format(dateFrom), sdf.format(dateTo)));
        sb.append("══════════════════════════════\n\n");

        // Секция закупок
        sb.append("────────── ЗАКУПКИ ──────────\n");
        double totalPurchases = 0;
        for (ProductArrivalTable arrival : arrivals) {
            double sum = arrival.quantity * arrival.buyPrice;
            sb.append(String.format(Locale.getDefault(),
                    "▸ %s: %s %d шт. × %d ₽ = %.2f ₽\n",
                    sdf.format(arrival.arrivalDate),
                    arrival.productName,
                    arrival.quantity,
                    arrival.buyPrice,
                    sum));
            totalPurchases += sum;
        }
        sb.append(String.format(Locale.getDefault(),
                "\nИТОГО ЗАКУПКИ: %.2f ₽\n\n", totalPurchases));

        // Секция продаж
        sb.append("────────── ПРОДАЖИ ──────────\n");
        double totalSales = 0;
        for (SaleProductTable sale : sales) {
            double sum = sale.saleQuantity * sale.salePrice;
            sb.append(String.format(Locale.getDefault(),
                    "▸ %s: %s %d шт. × %d ₽ (%s) = %.2f ₽\n",
                    sdf.format(sale.saleDate),
                    sale.productName,
                    sale.saleQuantity,
                    sale.salePrice,
                    sale.paymentMethod,
                    sum));
            totalSales += sum;
        }
        sb.append(String.format(Locale.getDefault(),
                "\nИТОГО ПРОДАЖИ: %.2f ₽\n\n", totalSales));

        // Итоговая информация
        sb.append("────────── ИТОГИ ───────────\n");
        sb.append(String.format(Locale.getDefault(),
                "МАРЖА: %.2f ₽\n", totalSales - totalPurchases));
        sb.append("══════════════════════════════");

        return sb.toString();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}