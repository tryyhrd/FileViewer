package com.example.fileviewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Объявление переменных класса
    private DatabaseHelper dbHelper;          // Для работы с локальной БД
    private RecyclerView recyclerViewGosts;   // Для отображения списка
    private GOSTAdapter gostAdapter;          // Адаптер для RecyclerView
    private List<GOST> gostList = new ArrayList<>(); // Список данных
    private APIService apiService;            // Для работы с API
    private ProgressBar progressBar;          // Индикатор загрузки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_page);

        // ШАГ 1: ИНИЦИАЛИЗАЦИЯ КОМПОНЕНТОВ
        initComponents();

        // ШАГ 2: НАСТРОЙКА RECYCLERVIEW
        setupRecyclerView();

        // ШАГ 3: ЗАГРУЗКА ДАННЫХ
        loadGostsFromAPI();
    }

    /**
     * ШАГ 1: Инициализация всех компонентов активности
     * На этом этапе мы находим View элементы и создаем экземпляры классов
     */
    private void initComponents() {
        // Инициализация базы данных (для локального хранения)
        dbHelper = new DatabaseHelper(this);

        // Инициализация API сервиса для работы с веб-сервером
        apiService = new APIService(this);

        // Находим RecyclerView в layout файле
        recyclerViewGosts = findViewById(R.id.gostEducation);

        // Находим ProgressBar для отображения загрузки
        progressBar = findViewById(R.id.progressBar);

        // Проверяем, что все компоненты найдены
        if (recyclerViewGosts == null) {
            Toast.makeText(this, "Ошибка: RecyclerView не найден", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ШАГ 2: Настройка RecyclerView
     * RecyclerView - это продвинутый ListView для отображения списков
     */
    private void setupRecyclerView() {
        // LinearLayoutManager располагает элементы в линейном порядке (вертикально)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGosts.setLayoutManager(layoutManager);

        // Создаем адаптер и передаем ему:
        // - список данных (пока пустой)
        // - обработчики кликов по элементам
        gostAdapter = new GOSTAdapter(gostList, new GOSTAdapter.OnItemClickListener() {
            /**
             * Обработчик обычного клика по элементу
             * @param gost - выбранный элемент ГОСТ
             */
            @Override
            public void onItemClick(GOST gost) {
                showShortGostInfo(gost);
            }

            /**
             * Обработчик долгого нажатия на элемент
             * @param gost - выбранный элемент ГОСТ
             */
            @Override
            public void onItemLongClick(GOST gost) {
                showDetailedGostInfo(gost);
            }
        });

        // Устанавливаем адаптер для RecyclerView
        recyclerViewGosts.setAdapter(gostAdapter);
    }

    /**
     * ШАГ 3: Загрузка данных из API
     * Этот метод выполняет асинхронный запрос к серверу
     */
    private void loadGostsFromAPI() {
        // Показываем индикатор загрузки
        showLoading(true);

        // Проверяем доступность интернета
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_LONG).show();
            showLoading(false);
            // Можно загрузить данные из локальной БД
            loadGostsFromLocalDB();
            return;
        }

        // Выполняем запрос к API
        apiService.getAllGosts(new APIService.GostListListener() {
            /**
             * Вызывается при успешной загрузке данных
             * @param gosts - список ГОСТов с сервера
             */
            @Override
            public void onSuccess(List<GOST> gosts) {
                // Обновляем UI в основном потоке
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading(false);

                        // Обновляем данные в адаптере
                        gostAdapter.updateData(gosts);

                        // Сохраняем данные в локальную БД для оффлайн-доступа
                        saveGostsToLocalDB(gosts);

                        // Показываем уведомление пользователю
                        Toast.makeText(MainActivity.this,
                                "Загружено ГОСТов: " + gosts.size(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * Вызывается при ошибке загрузки
             * @param error - текст ошибки
             */
            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading(false);

                        // Показываем ошибку
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();

                        // Пробуем загрузить из локальной БД
                        loadGostsFromLocalDB();
                    }
                });
            }
        });
    }

    /**
     * Загрузка данных из локальной базы данных
     * Используется при отсутствии интернета или ошибках API
     */
    private void loadGostsFromLocalDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<GOST> localGosts = dbHelper.getAllGosts();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (localGosts.isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    "Нет данных для отображения",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            gostAdapter.updateData(localGosts);
                            Toast.makeText(MainActivity.this,
                                    "Загружено из кэша: " + localGosts.size(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Сохранение данных в локальную базу
     * @param gosts - список ГОСТов для сохранения
     */
    private void saveGostsToLocalDB(List<GOST> gosts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper.saveGosts(gosts);
            }
        }).start();
    }

    /**
     * Управление видимостью индикатора загрузки
     * @param show - true показать, false скрыть
     */
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerViewGosts.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Показ краткой информации о ГОСТе
     * @param gost - объект ГОСТ для отображения
     */
    private void showShortGostInfo(GOST gost) {
        String info = gost.code + "\n" + gost.title;
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }

    /**
     * Показ подробной информации в диалоговом окне
     * @param gost - объект ГОСТ для отображения
     */
    private void showDetailedGostInfo(GOST gost) {
        String fullInfo = "Номер: " + gost.number + "\n\n" +
                "Код ГОСТа: " + gost.code + "\n\n" +
                "Название: " + gost.title + "\n\n" +
                "Описание: " + gost.description;

        // Создаем диалоговое окно
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Подробная информация о ГОСТе");
        builder.setMessage(fullInfo);
        builder.setPositiveButton("Закрыть", null); // Простая кнопка закрытия

        // Кнопка для sharing
        builder.setNeutralButton("Поделиться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareGostInfo(gost);
            }
        });

        builder.show(); // Показываем диалог
    }

    /**
     * Sharing информации о ГОСТе через другие приложения
     * @param gost - объект ГОСТ для sharing
     */
    private void shareGostInfo(GOST gost) {
        String shareText = gost.number + ". " + gost.code + "\n" +
                gost.title + "\n" +
                gost.description;

        // Создаем intent для sharing
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Информация о ГОСТе");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Запускаем диалог выбора приложения
        startActivity(Intent.createChooser(shareIntent, "Поделиться информацией о ГОСТе"));
    }

    /**
     * Очистка ресурсов при уничтожении активности
     */
    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close(); // Закрываем соединение с БД
        }
        super.onDestroy();
    }
}