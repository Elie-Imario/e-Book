package com.ebookutil.util;

import javafx.scene.control.Button;

public class BtnStyleCurrentView {

    static String activeBtn = """
    -fx-background-color: linear-gradient(to top,rgba(3, 48, 88, 0.5), rgb(0, 0, 30)), linear-gradient(to bottom,rgba(3, 48, 88, 0.5), rgb(0, 0, 30));
            -fx-border-color: rgb(255, 255, 255,.4) Transparent rgb(0, 0, 0,.2) WHITE ;
            -fx-border-width: 1 1 1 4;""";
    static String initBtnStyle = """
            -fx-background-color: Transparent;
            -fx-border-color: rgb(255, 255, 255,.4) Transparent rgb(0, 0, 0,.2) Transparent;
            -fx-border-width: 1;""";
    static String initBtnAccordionOuvrageStyle = """
            -fx-background-color: #093F76;
            -fx-border-color: #093F76;
            -fx-border-width: 1;""";
    static String initBtnAccordionReaderStyle = """
            -fx-background-color: #04224E;
            -fx-border-color: #04224E;
            -fx-border-width: 1;""";

    public static void pretViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(activeBtn);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(initBtnStyle);
    }

    public static void addBookViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(activeBtn);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(initBtnStyle);
    }

    public static void ListBookViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(activeBtn);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(initBtnStyle);
    }

    public static void annuaireSearchViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(activeBtn);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(initBtnStyle);
    }

    public static void monCompteViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(activeBtn);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(initBtnStyle);
    }

    public static void addReaderViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(activeBtn);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(initBtnStyle);
    }

    public static void listReaderViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(activeBtn);
        btn8.setStyle(initBtnStyle);
    }

    public static void ChartViewBtnStyle(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8){
        btn1.setStyle(initBtnStyle);
        btn2.setStyle(initBtnAccordionOuvrageStyle);
        btn3.setStyle(initBtnAccordionOuvrageStyle);
        btn4.setStyle(initBtnStyle);
        btn5.setStyle(initBtnStyle);
        btn6.setStyle(initBtnAccordionReaderStyle);
        btn7.setStyle(initBtnAccordionReaderStyle);
        btn8.setStyle(activeBtn);
    }
}
