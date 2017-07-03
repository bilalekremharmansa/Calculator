package com.example.bilalekremharmansa.calculator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//android gui log kütüphaneleri

public class MainActivity extends AppCompatActivity {

    private TextView txtOperationLine;
    final Calculator calculator = new Calculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOperationLine = (TextView) findViewById(R.id.txtOperationLine);


        int [] buttonResources = new int[]{R.id.btnNum0,R.id.btnNum1,R.id.btnNum2,R.id.btnNum3,R.id.btnNum4,R.id.btnNum5,R.id.btnNum6,
                                             R.id.btnNum7,R.id.btnNum8,R.id.btnNum9,R.id.btnAdd,R.id.btnSub,R.id.btnDiv,R.id.btnMul,R.id.btnComa,
                                                R.id.btnBracketO, R.id.btnBracketC, R.id.btnPlusMinus};

        View.OnClickListener customButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kullanıcın tıkladığı butonun texti alınır ve calculator nesnesinin appendOperationLine(char) methoduna gönderilir. Son olarak
                // txtOperationLine güncellemek için updateTxtOperationLineTxt() çağrılır.

                //hint kullanıldı.
                Button b =(Button)v;
                if( b.getHint() != null && b.getHint().charAt(0) == '_' ){
                    calculator.setNegativeFlagAsTrue();
                }else{
                    char c =  b.getText().charAt(0);
                    calculator.appendOperationLine(c);
                }

                updateTxtOperationLineText();

            }
        };

        //buttonResources içerisindeki butonların hepsinin listenerlarını set etmek için bu yapı kuruluyor.
        for (int IDs: buttonResources) {
            Button b = (Button) findViewById(IDs);

            b.setOnClickListener(customButtonListener);

        }

        // eşittir butonun listenırı
        ((Button) findViewById(R.id.btnEquals)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hesaplama işlemi yapılır.

                try{
                    calculator.evaluate();
                    updateTxtOperationLineText();
                } catch(SyntaxErrorException ex){
                    calculator.setCurrentResult(ex.getMessage());
                    updateTxtOperationLineText();
                    calculator.clearEveryting();
                }
            }
        });

        //AC butonu, ekrandaki ifadeleri temizler.


        ((Button) findViewById(R.id.btnClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b =(Button)v;
                calculator.deleteLastCharacterOfOperationLine();
                updateTxtOperationLineText();
            }
        });

        ((Button) findViewById(R.id.btnClear)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                calculator.clearEveryting();
                updateTxtOperationLineText();
                return true;
            }
        });
    }

    //txtOperationLine View nesnesinin text ini günceller. Kullanıcı eşittir butonuna bastığında 2 farklı string gözüküyor, üst satırda işlem sırası
    //alt satırda sonuç. Farklı boyut ve renklerde bunun düzenlemesini bu metot yapıyor.
    public void updateTxtOperationLineText(){

        //SpannableString bir string farklı karakterlerinin farklı boyut,renk vb özelliklerine sahip olmasını istersek kullandığımız yapıdır.
        //calculatordan operationLine ı alırken bir metot ile _ leri negatifliği belirlemek için - yapıyorum.
        SpannableString ssExpression=  new SpannableString(calculator.replaceUnderlineToMinusOpLine());
        //setSpan hangi karakterlere ve ne şekilde tesir ediceğini söyler. İlk parametrede hangi property nin değişmesini istiyorsak onu düzenleriz
        //ikinci ve üçüncü paremetreler sınırları belirler.
        ssExpression.setSpan(new RelativeSizeSpan(.75f), 0,ssExpression.length(), 0); // set size
        ssExpression.setSpan(new ForegroundColorSpan(Color.GRAY), 0, ssExpression.length(), 0);// set color

        //BİLAL BURAYI TEKRAR DÜŞÜN CALCULATOR NESNESİNDE BİR METOT İLE HALLEDİLEBİLİR..
        //Bu ifadenin sebebi ise AC butonu çalıştığında currentResult = 0 oluyor ve kullanıcı tekrardan sayılara tıklamaya başladığında
        // currentResult ın temizlenmesi gerekir ancak temizlenmek yerine 0 hala orada duruyor. Hoş bir görüntü olmadığı iiçin onun temizlenmesi gerek
        //bu nedenden ötürü içini boşaltıyoruz.

        if(calculator.getCurrentResult().equals("0") && !calculator.getOperationLine().toString().equals("")){

            calculator.setCurrentResult("");
        }

        //ssResult yorum satırı ile kapalı, eğer onunda farklı şekilde davranmasını ister isek onuda düzenleyebiliriz.
        //SpannableString ssResult=  new SpannableString(calculator.getCurrentResult());

        //burada iki text birleştiriliyor.
        CharSequence finalText = TextUtils.concat(ssExpression, "\n", calculator.getCurrentResult());

        //TextView i güncelleme işlemi yapılır
        txtOperationLine.setText(finalText);
    }
}
