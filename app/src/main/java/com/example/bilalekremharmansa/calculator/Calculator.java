package com.example.bilalekremharmansa.calculator;

import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by bilalekremharmansa on 29.6.2017.
 *
 * Android stuido ortamında geliştirilen Hesap Makinası
 */

public class Calculator {
    private StringBuilder operationLine;
    private String currentResult;
    private boolean resultFlag;
    private boolean negativeFlag;


    private int openBracketCounter;

    private OperatorStack operatorStack;


    private static final char COMA = '.';
     // farklı dillerde noktaya dönebilirç.

    private enum Operators {
        SUM, SUB, MUL, DIV, MINUS

    }

    public Calculator() {
        operationLine = new StringBuilder();
        operatorStack = new OperatorStack();

        currentResult = "";


    }

    //helper method
    private boolean isNumber(char c) {
        return (c >= 48 && c <= 57) ;
    }

    //helper method
    private boolean isOperator(char c) {
        //karakter bir operator mu diye kontrol edilir.
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    //helper method
    private boolean isBracket(char c) {
        //gelen karakter parantez mi kontrolü
        return c == '(' || c == ')';
    }

    //helper method
    private Operators getOperationType(char c) {
        //Gelen operatorun Operator enum turunde geri dönüşünü yapıyor. Aksi taktirde null dönüyor. Fonksiyon çağrıldığında null kontrolü yapılabilir.
        switch (c) {
            case '+':
                return Operators.SUM;
            case '-':
                return Operators.SUB;
            case '*':
                return Operators.MUL;
            case '/':
                return Operators.DIV;
        }
        return null;
    }

    //helper method
    private void giveSpaceIfNotExist(StringBuilder sb) {
        //parametre olarak aldığı stringBuilder nesnesinin son elemanı boşluk değilse boşluk ekliyor.
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') sb.append(" ");
    }

    //helper method
    public String replaceUnderlineToMinusOpLine() {
        return operationLine.toString().replaceAll("_", "-");
    }

    //helper method
    private boolean isStackPeekBigger(char c) throws EmptyStackException {
        //operatorStack in en üstündeki karakter gelen karakterden büyük veya eşit öncelikte ise geriye true aksi taktirde false döndürülür.
        if ((operatorStack.peek() == '*') || (operatorStack.peek() == '/'))
            return true;
        else if ((c == '+' || c == '-') && operatorStack.peek() == '+' || operatorStack.peek() == '-') {
            return true;
        }
        return false;
    }

    //helper method
    private char lastCharacterOfOperationLine() {
        try {
            return this.operationLine.charAt(this.operationLine.length() - 1);
        } catch (NullPointerException ex) {
            return ' ';
        } catch (StringIndexOutOfBoundsException ex) {
            return ' ';
        }

    }

    public char deleteLastCharacterOfOperationLine() {

        if (operationLine.length() > 0) {
            char c = lastCharacterOfOperationLine();

            if (c == '(') {
                openBracketCounter--;
            } else if (c == ')') {
                openBracketCounter++;
            }
            operationLine.deleteCharAt(operationLine.length() - 1);
            currentResult = ""; //silme işlemi gerçeklişirse currentResult ın temizlenmesi gerekiyor.
            resultFlag = false; //Silme işlemi gerçekleştikten sonra currentResult boş string olduğu için resultFlag in false olması gerekiyor.
                                //Buraya observer pattern-listener kullanarak, currentResult "" olduğunda resultFlag i hep false eden bi yapı kurulabilr.
                                //aynı şekilde benzer yapı olarak ')' '(' ifadelerini metot ile ekleyerek openBracketCounter daha dinamik olarak programnlanabilir
                                //
            return c;
        }
        return ' ';
    }

    public void clearEveryting() {
        operationLine = new StringBuilder();
        currentResult = "0";
        openBracketCounter = 0;
        resultFlag = false;
        negativeFlag = false;
    }

    private StringBuilder infixToPostFix() {
        //Infix ten postfixe dönüştürme işlemi yapılırken, sayılar ve operandlar arasında bir boşluk bırakılarak dönüştürme yapılmıştır.
        StringBuilder sb = new StringBuilder();

        while (openBracketCounter > 0) {
            appendOperationLine(')');
        }


        for (int i = 0; i < operationLine.length(); i++) {
            char c = operationLine.charAt(i);

            if (isBracket(c)) {//Eğer okunan karakter açık parantez ise sorgusuz sualsiz ekle, kapalı parantez ise açık parantez görene kadar
                //stacktekileri postfix ifadesine pop et.

                if (c == '(') {
                    operatorStack.push(c);
                } else {//c==')'

                    while (operatorStack.peek() != '(') {
                        giveSpaceIfNotExist(sb);
                        sb.append(operatorStack.pop());
                    }
                    giveSpaceIfNotExist(sb);
                    operatorStack.pop();
                }
            } else if (isOperator(c)) { //Eğer okunan karakter Operator ise (*,+,-,/) stackteki önceliğine bakılarak pop push işlemleri yapılır.

                try {
                    //Stackteki önceliği büyük ve eşit olduğu sürece, postfix ifadesine stackten pop edilir.
                    while (isStackPeekBigger(c)) {
                        giveSpaceIfNotExist(sb);
                        sb.append(operatorStack.pop());
                    }

                } catch (EmptyStackException esx) {
                    //esx.printStackTrace();
                    //bir şey yapılmasına gerek yok.
                }

                //pop işlemi yapılsın yapılmasın gelen operator stacke push edilir.
                operatorStack.push(c);
                //Postfix ifadesine dönüştürülürken elimizdeki sayıları yan yana koyuyoruz. aBu dönüşüm sırasında 34 + 4 ifadesi 344+ ifadesine
                // dönüşür. Bu süreçte 3 + 44 mu 34 + 4 mu yapıldığı belirsiz olur. Bu nedenden ötürü operatorler stacke push edilmeden önce
                // araya ayırmak için bir boşluk koyduk.
                giveSpaceIfNotExist(sb);

            } else {//okunan karakter sayı veya virgül ise postfix ifadesine ekleme yap.
                sb.append(c);

            }

        }
        //stackte boşta operator kalmayana kadar pop edilir.
        while (!operatorStack.empty())

        {
            giveSpaceIfNotExist(sb);
            sb.append(operatorStack.pop());
        }


        return sb;
    }

    //Hesap makinasının hesaplama methodu
    public String evaluate() {
        //private olarak BigDecimal nesnesi döndüren evaluate methoduna postfix stringini geçerek geriye sonucu string olarak alıyor.

        currentResult = evaluate(infixToPostFix().toString()).stripTrailingZeros().toPlainString();

        //resultFlag in tutulma sebebi AC tuşuna basıldığında currentResult 0 olarak set ediliyor. Sonrasında sayılara tıklandığında
        //operationLine ın başına sıfır koyuyor. Bunun önüne geçilmek için flag bilgisi.
        // Bkz: AC  basıldı. currentResult ın valuesi 0. 5 butonu clicklendi. operationLine = 05 oldu. Bunun önüne geçmek için.
        //appendOperationLine methodunda if sorgusu ile bu işlem yapılır.
        resultFlag = true;
        return currentResult;
    }

    private BigDecimal evaluate(String postfix) {
        //Postfix ifade dönüştürülmeye başlanıyo
        Stack<BigDecimal> bigDecimalStack = new Stack<>();
        //postfix ifademiz boşluklardan oluşuyor(operator ve operandlar(sayılar) arasında boşluklar var), her bir ifadeyi tek tek işliyoruz.
        for (String expression : postfix.split(" ")) {
            //ifade operator ise stackin üstündeki 2 sayı pop edilerek(ters sıra ile) operatore göre işlem yapılır ve tekrar stacke push edilir.
            char c = expression.charAt(0);
            if (isOperator(c)) {
                Operators op = getOperationType(expression.charAt(0));

                try {
                    BigDecimal numberTwo = bigDecimalStack.pop();
                    BigDecimal numberOne = bigDecimalStack.pop();

                    switch (op) {
                        case SUM:
                            bigDecimalStack.push(add(numberOne, numberTwo));
                            break;
                        case SUB:
                            bigDecimalStack.push(subtract(numberOne, numberTwo));
                            break;
                        case MUL:
                            bigDecimalStack.push(multiply(numberOne, numberTwo));
                            break;
                        case DIV:
                            bigDecimalStack.push(divide(numberOne, numberTwo));
                            break;
                    }


                } catch (EmptyStackException ex) {
                    ex.printStackTrace();
                    break;
                }catch (NullPointerException ex){
                    break;
                }


            } else {//gelen ifade sayı ise stacke push edilir.
                //sayının eksi olup olmadığı _ den belli olduğu için( _ negatifliği belirtiyor) _ yi BigDecimal nesnesinin anlaması için - ye çevirmemiz gerek.
                if (c == '_') {
                    expression = expression.replace('_', '-');
                }
                bigDecimalStack.push(new BigDecimal(expression));
            }


        }

        return bigDecimalStack.pop();

    }

    // BigDecimal toplama
    private BigDecimal add(BigDecimal numOne, BigDecimal numTwo) {
        return numOne.add(numTwo);
    }

    // BigDecimal çıkarma
    private BigDecimal subtract(BigDecimal numOne, BigDecimal numTwo) {

        return numOne.subtract(numTwo);
    }

    // BigDecimal çarpma
    private BigDecimal multiply(BigDecimal numOne, BigDecimal numTwo) {
        return numOne.multiply(numTwo);
    }

    // BigDecimal bölme
    private BigDecimal divide(BigDecimal numOne, BigDecimal numTwo) throws SyntaxErrorException {
        if (numTwo.intValue() == 0) {
            throw new SyntaxErrorException("Sıfıra bölünemez.");
        } else {
            return numOne.divide(numTwo, 5, BigDecimal.ROUND_HALF_UP);
        }
    }


    public void appendOperationLine(char c) {

        //resultFlag in ne işe yaradığı evaluate() te belirtildi. Orda belirtilen işleme göre flag set ise;
        if (resultFlag) {
            resultFlag = false; //eski konumuna döndrülür.

            //gelen karakter sayı ise. sayının operationLine a çıkması gerekir ve sonuc stringinin temizlenmesi gerekir.
            //bu senaryoda hesaplanan result bi yere transfer edilmez, silinir.
            if (isNumber(c)) {
                operationLine = new StringBuilder(c);
                currentResult = "";
            }
            //gelen karakter açık parantez ise hesaplanan sayı parantezle birlikte üst satıra çıkar.
            //hesaplanan sonuc 5 ise  (5 şekilne dönmeli. Kapalı parantez gelir ise işlem yapılmaz.
            else if (isBracket(c)) {
                if (c == '(') {
                    operationLine = new StringBuilder(c + currentResult.replace("-", "_"));
                    openBracketCounter++;
                    currentResult = "";
                }else{// c==')' ise resultFlag in hala true kalması gerekiyor. Çünkü ) gelirse hiç bi işlem yapılmaması ve resultFlag davranışının
                    //değişmemesi gerekiyor.
                    resultFlag = true;
                }

            } else {
                //gelen karakter sayı veya nokta-virgül veya operator ise, sonucu direk operationLine a taşı ve ekleme recursive olarak tekrar çağır
                operationLine = new StringBuilder(currentResult.replace("-", "_"));
                currentResult = "";
                appendOperationLine(c);
            }
        }

        //negativeFlag set olduysa yani kullanıcı +/- butonuna tıklayarak negatif sayı girmek istiyorsa;
        if (negativeFlag) {
            //bunun için bazı koşullar var, ya operationLine da hiç bir karakter olmayak, ya operatorLine ın son karakteri
            //bir oparetor yada açık parantez olması gerekir. Aksi taktirde işlem yapılmaz. Bu koşullardan biri sağlaırsa.
            //Sayı _ ile birlikte operationLinea eklenir. operationLine daki _ ler sayının negatifliğini temsil eder. - olan
            //operator ile karıştırılmaması için böyle ypaıldı.
            if (operationLine.length() == 0 || isOperator(lastCharacterOfOperationLine()) || lastCharacterOfOperationLine() == '(') {
                negativeFlag = false;
                this.operationLine.append('(');
                this.operationLine.append('_');
                this.operationLine.append(c);
                this.operationLine.append(')');
            }
        }
        //Normal eklemeler devam ediyor burdan
        //gelen sayı parantez ise;
        if (isBracket(c)) {
            //açık parantez ise;
            if (c == '(') {
                //her eklemede openBracketCounter ı arttırdığımıza dikkat edelim. kapalı parantezler bu koşula göre eklenir.
                //eğer operationLine da tek bir sayı var ise ve bu sayı 0 ise parantez sıfırdan önce eklenir yani (0
                if (operationLine.length() == 1 && operationLine.charAt(0) == '0') {
                    openBracketCounter++;
                    this.operationLine.insert(0, c);
                }
                //son karakter bir operator ise direk ekleme işlemi yapılır.
                else if (isOperator(lastCharacterOfOperationLine())) {
                    openBracketCounter++;
                    this.operationLine.append(c);
                } else if (lastCharacterOfOperationLine() == ' ' || lastCharacterOfOperationLine() == '(') {
                    //lastCharacterOfOperatioLine() eğer catch e düşerse ' ' döndürüyor. ' ' a düşme sebepleride null gelmesi veya son karakterin olmaması(yani opLine ın
                    //lengthi 0 olduğu için son index diye bir index yok. Bu yüzden boş ise parantez açılabilmeli.
                    openBracketCounter++;
                    this.operationLine.append(c);
                }

            } else {// c==')'
                //kapalı parantez geldiğinde, infixPostfix dönüşümünde programın crashlenmemesi için, () şeklinde bir ifade
                //bırakmıyoruz. Böyle bir şey geldiğinde program otomatik olarak (0) a dönüştürüyor.
                if (lastCharacterOfOperationLine() == '(') {
                    this.operationLine.append('0');
                    openBracketCounter--;
                    this.operationLine.append(c);
                } else if (openBracketCounter > 0) {
                    //açık parantez olduğu müddetçe kapalı parantez eklenebilir.
                    openBracketCounter--;
                    this.operationLine.append(c);
                }


            }


        } else if (isNumber(c)) {//gelen karakter sayı ise
            char lastCharacterOfOpLine = lastCharacterOfOperationLine();
            if (lastCharacterOfOpLine == ')') {
                // son karakter sayı ) ise ekleme yapılmaz
            } else if (lastCharacterOfOpLine == '0') {
                //son karakter 0 ise yeni gelen karakter 0 ile yer değiştirilir.
                this.operationLine.replace(this.operationLine.length() - 1,
                        this.operationLine.length(), String.valueOf(c));
            } else {
                //geri kalan her koşulda sayı eklenibilir.
                this.operationLine.append(c);
            }

        } else if (isOperator(c)) {//gelen karakter operator ise
            char lastCharacterOfOpLine = lastCharacterOfOperationLine();
            if (isOperator(lastCharacterOfOpLine)) {
                //son karakter operator ise yeni gelen operator ile son operatoru değiştir.
                this.operationLine.replace(this.operationLine.length() - 1,
                        this.operationLine.length(), String.valueOf(c));
            }
            //else if (lastCharacterOfOpLine == '(') { açık parantezden sonra operator eklenemez.}
            else {
                //diğer koşullarda ekleme yapılabilir.
                this.operationLine.append(c);
            }

        } else if (c == COMA) {
            //gelen karakter virgül-nokta ise son karaktere bakılır sayı ise virgül eklenebilir değil ise işlem yapılmaz.
            if (isNumber(lastCharacterOfOperationLine())) {
                this.operationLine.append(c);
            }


        }


    }


    // ---------------------------- GETTER SETTER ----------------------------
    public String getCurrentResult() {
        return currentResult;
    }

    public void setCurrentResult(String currentResult) {
        this.currentResult = currentResult;
    }

    public StringBuilder getOperationLine() {

        return operationLine;
    }

    public void setOperationLine(StringBuilder operationLine) {
        this.operationLine = operationLine;

    }

    public void setOperationLine(String operationLine) {
        this.operationLine = new StringBuilder(operationLine);

    }

    public void setNegativeFlagAsTrue() {
        this.negativeFlag = true;
    }

}
