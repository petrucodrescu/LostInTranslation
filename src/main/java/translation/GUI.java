package translation;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();
            Translator translator = new JSONTranslator();

            List<String> languageNames = new ArrayList<>();
            for (String code : translator.getLanguageCodes()) {
                String lang = languageConverter.fromLanguageCode(code);
                if (lang != null) {
                    languageNames.add(lang);
                }
            }

            // Language dropdown
            JPanel languagePanel = new JPanel();
            JComboBox<String> languageBox = new JComboBox<>(
                    languageNames.toArray(new String[0])
            );
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageBox);


            List<String> countryNames = new ArrayList<>();
            for (String code : translator.getCountryCodes()) {
                String country = countryConverter.fromCountryCode(code);
                if (country != null) {
                    countryNames.add(country);
                }
            }

            // Country list (always open with scroll)
            JList<String> countryList = new JList<>(countryNames.toArray(new String[0]));
            countryList.setVisibleRowCount(8); // always show ~8 countries before scrolling
            JScrollPane countryScroll = new JScrollPane(countryList);

            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new BoxLayout(countryPanel, BoxLayout.Y_AXIS));
            countryPanel.add(countryScroll);


            // adding listener for when the user clicks the submit button
//            submit.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    String language = languageBox.getText();
//                    String country = countryField.getText();
//
//                    // for now, just using our simple translator, but
//                    // we'll need to use the real JSON version later.
//                    Translator translator = new CanadaTranslator();
//
//                    String result = translator.translate(country, language);
//                    if (result == null) {
//                        result = "no translation found!";
//                    }
//                    resultLabel.setText(result);
//
//                }
//
//            });

            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

            JLabel resultTitle = new JLabel("Translation:"); // NEW
            JLabel resultLabel = new JLabel(" "); // placeholder text // NEW

            resultPanel.add(resultTitle); // NEW
            resultPanel.add(resultLabel); // NEW

            // Update translation when either selection changes
            Runnable updateTranslation = () -> {
                String languageName = (String) languageBox.getSelectedItem();
                String countryName = countryList.getSelectedValue();

                if (languageName != null && countryName != null) {
                    String languageCode = languageConverter.fromLanguage(languageName);
                    String countryCode = countryConverter.fromCountry(countryName);

                    String result = translator.translate(countryCode, languageCode);
                    if (result == null) {
                        result = "No translation found!";
                    }
                    resultLabel.setText(result);
                }
            };

            // Add listeners
            languageBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateTranslation.run();
                }
            });
            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    updateTranslation.run();
                }
            });

            // Main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);
            mainPanel.add(countryPanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
