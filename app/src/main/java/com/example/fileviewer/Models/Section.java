package com.example.fileviewer.Models;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Section implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("document_id")
    public int document_id;

    @SerializedName("title")
    public String title;

    @SerializedName("content")
    public String content;

    @SerializedName("order")
    public int order;

    @SerializedName("is_deleted")
    public boolean is_deleted;

    public Section() {
    }

    public Section(int id, int document_id, String title, String content, int order, boolean is_deleted) {
        this.id = id;
        this.document_id = document_id;
        this.title = title;
        this.content = content;
        this.order = order;
        this.is_deleted = is_deleted;
    }

    // Для обычного TextView
    public CharSequence getFormattedContent() {
        if (content == null) return "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(content);
        }
    }

    // Для обычного TextView
    public CharSequence getFormattedTitle() {
        if (title == null || title.isEmpty()) {
            return "";
        }

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(title);
            }
        } catch (Exception e) {
            return title;
        }
    }

    // ДЛЯ WEBVIEW - метод для получения HTML с красными строками
    public String getHtmlFormattedContent() {
        if (content == null || content.isEmpty()) {
            return "";
        }

        return createSimpleJustifiedHtml(content);
    }

    // Упрощенная версия без сложного форматирования
    private String createSimpleJustifiedHtml(String content) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: sans-serif;\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333333;\n" +
                "            text-align: justify;\n" +
                "            margin: 0;\n" +
                "            padding: 8px 4px;\n" +
                "            background: white;\n" +
                "        }\n" +
                "        p {\n" +
                "            text-indent: 1.5em;\n" +
                "            margin-bottom: 0.8em;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div>" + content.replace("\n", "<br>") + "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String formatContentWithParagraphs(String content) {
        if (content == null || content.isEmpty()) {
            return "<p class=\"paragraph\"></p>";
        }

        // Сохраняем оригинальные HTML теги, включая <br>
        String preservedContent = preserveOriginalFormatting(content);

        // Очищаем контент, но сохраняем HTML теги
        String cleanedContent = preservedContent.replaceAll("\r\n", "\n")
                .replaceAll("\r", "\n")
                .replaceAll("\n{3,}", "\n\n")
                .trim();

        String[] paragraphs = cleanedContent.split("\n\n|\n");
        StringBuilder result = new StringBuilder();

        for (String paragraph : paragraphs) {
            String trimmed = paragraph.trim();
            if (!trimmed.isEmpty()) {
                // Определяем тип абзаца
                boolean isHeading = isLikelyHeading(trimmed);
                boolean isList = isLikelyList(trimmed);

                if (isHeading) {
                    result.append("<p class=\"no-indent\">")
                            .append(trimmed) // Не экранируем HTML, сохраняем теги
                            .append("</p>");
                } else if (isList) {
                    result.append("<p class=\"no-indent\">")
                            .append(trimmed) // Не экранируем HTML, сохраняем теги
                            .append("</p>");
                } else {
                    result.append("<p class=\"paragraph\">")
                            .append(trimmed) // Не экранируем HTML, сохраняем теги
                            .append("</p>");
                }
            }
        }

        return result.toString();
    }

    private String preserveOriginalFormatting(String content) {
        if (content == null) return "";

        // Заменяем одиночные переносы на <br> теги
        String withBreaks = content.replace("\n", "<br>");

        // Убеждаемся, что HTML теги корректны
        return withBreaks.replace("<br><br>", "<br><br>")
                .replace("<br><br><br>", "<br><br>");
    }

    private boolean isLikelyHeading(String text) {
        if (text == null || text.isEmpty()) return false;

        // Убираем HTML теги для анализа
        String plainText = text.replaceAll("<[^>]*>", "").trim();

        if (plainText.isEmpty()) return false;

        // Эвристика для определения заголовков:
        return plainText.length() < 100 &&
                !plainText.endsWith(".") &&
                !plainText.endsWith(",") &&
                Character.isUpperCase(plainText.charAt(0)) &&
                !plainText.contains(". ") &&
                plainText.split(" ").length < 12;
    }

    private boolean isLikelyList(String text) {
        if (text == null || text.isEmpty()) return false;

        // Убираем HTML теги для анализа
        String plainText = text.replaceAll("<[^>]*>", "").trim();

        if (plainText.isEmpty()) return false;

        return plainText.startsWith("-") ||
                plainText.startsWith("•") ||
                plainText.startsWith("*") ||
                plainText.matches("^\\d+\\.\\s.*") ||
                plainText.matches("^[a-zA-Z]\\.\\s.*");
    }
}