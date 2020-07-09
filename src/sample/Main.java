package sample;
import java.io.*;

import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.event.*;
import javafx.scene.paint.Color;

public class Main extends Application {
    //文件
    public String path = null ;
    //寻找
    int index = 0;
    //字体
    String style = "STSong";
    int size = 10;
    Font font = new Font(style,size);
    //颜色
    String scolor;
    Color color = Color.BLACK;

    //底部显示栏
    Label labelw ;
    Label labell ;
    Label labelc ;

    //文本编辑窗
    TextArea textarea = new TextArea();

    public static void main(String args[]) {
        launch(args);
    }
    public void start(Stage mystage) throws Exception {
        // TODO 自动生成的方法存根

        mystage.setTitle("菜鸟编辑器");  //名字
        BorderPane root = new BorderPane(); //布局
        Scene mysence = new Scene(root,1000,500); //大小

        //文本编辑窗
        root.setCenter(textarea);

        //第一项大菜单-文件（5个功能）
        Menu menufile = new Menu("文件");

        MenuItem newfile = new MenuItem("新建");
        newfile.setOnAction((ActionEvent e)->{
            textarea.setText("");
            mystage.setTitle("我的新笔记本");
            path = null;
        });
        //打开
        MenuItem open = new MenuItem("打开");
        open.setOnAction((ActionEvent e)->{
            FileChooser f = new FileChooser();//文件选择器
            f.setTitle("打开");
             File file = f.showOpenDialog(mystage);
            if (file!=null&&file.exists()) {
                try {
                    FileInputStream in = new FileInputStream(file);
                    InputStreamReader isr=new InputStreamReader(in,"GBK");//字节流与字符流的转换器
                    char[] ch=new char[(int)file.length()];//建立文档字符长度相同的数组
                    int len=isr.read(ch);
                    textarea.setText(new String(ch,0,len));
                    isr.close();
                    // 读取数据 放进多行文本框
                    in.close();
                    // 将文件路径 保留到成员变量path中
                    path = file.getPath();
                    int lastindex = path.lastIndexOf("\\"); // 查找字符串最后一次出现的位置
                    String title = path.substring(lastindex + 1); // 从下标lastIndex开始到结束产生一个新的字符串
                    mystage.setTitle(title + "-记事本");
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //保存
        MenuItem save = new MenuItem("保存");
        save.setOnAction((ActionEvent e)->{
            if (path == null) {
                FileChooser fc = new FileChooser();
                fc.setTitle("选择文件-保存");
                // 获取被用户选择的文件
                File file = fc.showSaveDialog(mystage);
                if (file != null && file.exists()) {
                    try {
                        // 创建输出流
                       FileOutputStream out = new FileOutputStream(file);
                       OutputStreamWriter osw = new OutputStreamWriter(out,"GBK");
                        osw.write(textarea.getText().replace("\n","\r\n"));
                        osw.close();
                        out.flush();
                        out.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else try {
                    // 创建输出流
                    FileOutputStream out = new FileOutputStream(file);
                    OutputStreamWriter osw = new OutputStreamWriter(out,"GBK");
                    osw.write(textarea.getText().replace("\n","\r\n"));
                    osw.close();
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {// 打开之后的保存
                try {
                    // 创建输出流
                    FileOutputStream out = new FileOutputStream(path);
                    OutputStreamWriter osw = new OutputStreamWriter(out,"GBK");
                    osw.write(textarea.getText().replace("\n","\r\n"));
                    osw.close();
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //另存为
        MenuItem asave = new MenuItem("另存为");
        asave.setOnAction((ActionEvent e)->{
            FileChooser f = new FileChooser();
            f.setTitle("另存为");
            File file = f.showSaveDialog(mystage);
            if (file!=null&&!file.exists()) {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    OutputStreamWriter osw = new OutputStreamWriter(out,"GBK");
                    osw.write(textarea.getText().replace("\n","\r\n"));//JAVA中换行不支持\n要用转义字符\r\n
                    osw.close();
                    out.flush();
                    out.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //退出
        MenuItem out = new MenuItem("退出");
        out.setOnAction((ActionEvent e)->{
            exit();
        });
        menufile.getItems().addAll(newfile,open,save,asave,out);

        //第二项大菜单-编辑（7个功能）
        Menu menuedit = new Menu("编辑");
        //剪切
        MenuItem cut = new MenuItem("剪切");
        cut.setOnAction((ActionEvent e)->{
            textarea.cut();
            textarea.replaceSelection(" ");
        });
        //复制
        MenuItem copy = new MenuItem("复制");
        copy.setOnAction((ActionEvent e)->{
            textarea.copy();
        });
        //撤销
        MenuItem undo = new MenuItem("撤销");
        copy.setOnAction((ActionEvent e)->{
                       textarea.undo();
        });
        //恢复
        MenuItem redo = new MenuItem("恢复");
        redo.setOnAction((ActionEvent e)->{
            textarea.redo();
        });
        //寻找
        MenuItem find = new MenuItem("寻找");
        find.setOnAction((ActionEvent e)->{
            findmethod();
        });
        //替换
        MenuItem replace = new MenuItem("替换");
        replace.setOnAction((ActionEvent e)->{
            replace();
        });
        //大小写替换
        MenuItem change = new MenuItem("大小写替换");
        change.setOnAction((ActionEvent e)->{
            change();
        });
        menuedit.getItems().addAll(cut,copy,undo,redo,find,replace,change);

        //第三项大菜单-统计（3个功能）
        Menu menucount = new Menu("统计");
        //字数
        MenuItem words = new MenuItem("字数");
        words.setOnAction((ActionEvent e)->{
            File file = new File(path);
            if(file!=null && file.exists()){
                try{
                    FileReader f = new FileReader(file);
                    BufferedReader up = new BufferedReader(f);
                    int line = 0;
                    while(up.read()!=-1)
                    {
                        String s = up.readLine();
                        line++;
                    }
                    String sun = textarea.getText();
                   int  word = sun.length();
                    //显示
                   // if(line == 1) {
                        Alert ln = new Alert(AlertType.CONFIRMATION);
                        ln.titleProperty().set("字数");
                        ln.headerTextProperty().set("字数为：" + (word-line+1));
                        labelw.setText("字数"+String.valueOf(word-line+1));
                        ln.show();
                        f.close();
                }catch(Exception ex){
                    ex.getStackTrace();
                }
            }

        });
        //行数
        MenuItem linenum = new MenuItem("行数");
        linenum.setOnAction((ActionEvent e)->{
            File file = new File(path);
            if(file!=null && file.exists()){
                try{
                    FileReader f = new FileReader(file);
                    BufferedReader up = new BufferedReader(f);
                    int line = 0;
                    while(up.read()!=-1)
                    {
                        String s = up.readLine();
                        line++;
                    }
                    Alert ln = new Alert(AlertType.CONFIRMATION );
                    ln.titleProperty().set("行数");
                    ln.headerTextProperty().set("行数为："+line);
                    labell.setText("    行数"+String.valueOf(line));
                    ln.show();
                    f.close();

                }catch(Exception ex){
                    ex.getStackTrace();
                }
            }
        });
        //字符数
        MenuItem charnum = new MenuItem("字符数");
        charnum.setOnAction((ActionEvent e)->{
            int charNum = textarea.getText().length();
            Alert ln = new Alert(AlertType.CONFIRMATION );
            ln.titleProperty().set("字符数");
            ln.headerTextProperty().set("字符数为："+charNum);
            labelc.setText("    字符数"+String.valueOf(charNum));
            ln.show();
        });
        menucount.getItems().addAll(words,charnum,linenum);

        //第四大类-格式（四个功能）
        Menu menuform = new Menu("格式");
        //字体格式
        MenuItem wset = new MenuItem("字体设置");
        wset.setOnAction((ActionEvent e)->{
            HBox box1 = new HBox();
            box1.setPadding(new Insets(10, 5, 20, 5));
            box1.setSpacing(5);
            Label lable1 = new Label("字体格式：");
            box1.getChildren().addAll(lable1);
            ChoiceBox wc =new ChoiceBox(FXCollections.observableArrayList("null","粗体", "楷书", "宋体"));
            VBox box2 = new VBox();
            box2.setPadding(new Insets(10, 5, 20, 10));
            box2.getChildren().addAll(wc);

            VBox box3 = new VBox();
            box3.setPadding(new Insets(10, 5, 20, 10));
            Button button1 = new Button("确定");
            box3.getChildren().add(button1);

            Stage Stage1 = new Stage();
            HBox hroot = new HBox();
            hroot.getChildren().addAll(box1, box2,box3);
            Scene scene1 = new Scene(hroot, 350, 50);
            Stage1.setTitle("格式");
            Stage1.setScene(scene1);
            Stage1.setResizable(false);

            button1.setOnAction((ActionEvent e1) -> {

                String mode = (String) wc.getValue();
                if((mode.equals("null"))){
                    textarea.setStyle("-fx-font-weight:"+"null");
                }
                else if((mode.equals("粗体"))) {
                    textarea.setStyle("-fx-font-weight:"+"bold");
                }
                else if((mode.equals("楷体"))) {
                    style = "KaiTi";
                    Font font1 = new Font(style, size);
                    textarea.setFont(font1);
                }
                else if((mode.equals("宋体"))) {
                    style ="STSong";
                    Font font1 = new Font(style, size);
                    textarea.setFont(font1);
                }
                Stage1.close();
            });
            Stage1.show();
        });
        //字体大小
        MenuItem wsize = new MenuItem("字体大小");
        wsize.setOnAction((ActionEvent e)-> {
            HBox box1 = new HBox();
            box1.setPadding(new Insets(10, 5, 20, 5));
            box1.setSpacing(5);
            Label lable1 = new Label("字体大小：");
            box1.getChildren().addAll(lable1);
            TextField cbox = new TextField();
            VBox box2 = new VBox();
            box2.setPadding(new Insets(10, 5, 20, 10));
            box2.getChildren().addAll(cbox);

            VBox box3 = new VBox();
            box3.setPadding(new Insets(10, 5, 20, 10));
            Button button1 = new Button("确定");
            box3.getChildren().add(button1);

            Stage Stage1 = new Stage();
            HBox hroot = new HBox();
            hroot.getChildren().addAll(box1, box2, box3);
            Scene scene1 = new Scene(hroot, 350, 50);
            Stage1.setTitle("格式");
            Stage1.setScene(scene1);
            Stage1.setResizable(false);
            button1.setOnAction((ActionEvent e1) -> {
                String size = cbox.getText();
                textarea.setStyle("-fx-font-size:" + size);
                textarea.setText(textarea.getText());
                 Stage1.close();
            });
            Stage1.show();
            });

            //字体颜色
            MenuItem wcolor = new MenuItem("字体颜色");
            wcolor.setOnAction((ActionEvent e) -> {
                HBox box1 = new HBox();
                box1.setPadding(new Insets(10, 5, 20, 5));
                box1.setSpacing(5);

                Label lable1 = new Label("字体颜色：");
                box1.getChildren().addAll(lable1);

                ColorPicker cp = new ColorPicker();
                cp.setValue(color);
                VBox box2 = new VBox();
                box2.setPadding(new Insets(10, 5, 20, 10));
                box2.getChildren().addAll(cp);

                VBox box3 = new VBox();
                box3.setPadding(new Insets(10, 5, 20, 10));
                Button button1 = new Button("确定");
                box3.getChildren().add(button1);

                Stage Stage1 = new Stage();
                HBox hroot = new HBox();
                hroot.getChildren().addAll(box1,box2,box3);
                Scene scene1 = new Scene(hroot, 350, 50);
                Stage1.setTitle("格式");
                Stage1.setScene(scene1);
                Stage1.setResizable(false);
               button1.setOnAction((ActionEvent e1) -> {
                   color = cp.getValue();
                   String sc = choosecolor(color);
                   textarea.setStyle("-fx-text-fill:"+sc);
                    Stage1.close();
    });
                Stage1.show();
});

        //背景颜色
        MenuItem bcolour = new MenuItem("背景颜色");
        bcolour.setOnAction((ActionEvent e) -> {
        HBox box1 = new HBox();
        box1.setPadding(new Insets(10, 5, 20, 5));
        box1.setSpacing(5);
        Label lable1 = new Label("背景颜色：");
        box1.getChildren().addAll(lable1);
        TextField cbox = new TextField();
        VBox box2 = new VBox();
        box2.setPadding(new Insets(10, 5, 20, 10));
        box2.getChildren().addAll(cbox);

        VBox box3 = new VBox();
        box3.setPadding(new Insets(10, 5, 20, 10));
        Button button1 = new Button("确定");
        box3.getChildren().add(button1);

        Stage Stage1 = new Stage();
        HBox hroot = new HBox();
        hroot.getChildren().addAll(box1, box2, box3);
        Scene scene1 = new Scene(hroot, 350, 50);
        Stage1.setTitle("格式");
        Stage1.setScene(scene1);
        Stage1.setResizable(false);
        button1.setOnAction((ActionEvent e1) -> {
        String bcolor = cbox.getText();
        textarea.setStyle("-fx-background-color:"+bcolor);
        Stage1.close();
        });
        Stage1.show();
        });
        menuform.getItems().addAll(wset,wsize,wcolor,bcolour);
        //菜单栏管理
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menufile, menuedit, menucount, menuform);
        //底部显示栏
        HBox down = new HBox();
         labelw = new Label("字数     ");
         labell = new Label("行数     ");
         labelc = new Label("字符数    ");
        down.getChildren().addAll(labelw, labell, labelc);
        down.setPrefSize(0, 10);

        root.setBottom(down);
        root.setTop(menuBar);
        mystage.setScene(mysence);
        mystage.show();
        }
//退出
private void exit(){
    HBox box1 = new HBox();
    box1.setPadding(new Insets(10,10,10,10));
    box1.setSpacing(5);
    box1.resize(100,50);

    Button btn1 = new Button("确定");
    box1.setPadding(new Insets(25,10,10,60));
    Button btn2 = new Button("取消");
    btn1.setOnAction((ActionEvent e)->{
        Platform.exit();
    });
    box1.getChildren().addAll(btn1, btn2);
    Stage findStage = new Stage();
    Scene scene1 = new Scene(box1, 200, 80);
    findStage.setTitle("退出");
    findStage.setScene(scene1);
    findStage.setResizable(false); // 固定窗口大小
    findStage.show();
    btn2.setOnAction((ActionEvent e)->{
       findStage.close();
    });
}
//寻找方法
private void findmethod(){
        //构建寻找窗口
        HBox box1 = new HBox();
        box1.setPadding(new Insets(10,10,10,10));
        box1.setSpacing(5);
        box1.resize(100,50);
        Label lable1 = new Label("查找内容(N):");
        lable1.setPadding(new Insets(4,0,0,1));
        TextField tf1 = new TextField();
        box1.getChildren().addAll(lable1, tf1);

        VBox box2 = new VBox();
        box2.setPadding(new Insets(10, 10, 10, 10));
        Button btn1 = new Button("查找下一个(F)");
        box2.getChildren().add(btn1);
        HBox findRootNode = new HBox();
        findRootNode.getChildren().addAll(box1, box2);

        Stage findStage = new Stage();
        Scene scene1 = new Scene(findRootNode, 450, 90);
        findStage.setTitle("查找");
        findStage.setScene(scene1);
        findStage.setResizable(false); // 固定窗口大小
        findStage.show();

        //寻找的功能
        btn1.setOnAction((ActionEvent e) -> {
        String textString = textarea.getText(); // 获取记事本文本域的字符串
        String tfString = tf1.getText(); // 获取要查找的字符串
        //输入是否为空
        if (!tf1.getText().isEmpty()) {
        //包含
        if (textString.contains(tfString)) {
        // 查找方法
        index = textarea.getText().indexOf(tf1.getText(), index );
        if (index == -1) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("已经找不到相关内容了");
        alert1.show();
        }
        if (index >= 0 && index < textarea.getText().length()) {
        textarea.selectRange(index, index + tf1.getText().length());
        index += tf1.getText().length();
        }
        }
        else  if (!textString.contains(tfString)) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("找不到相关内容了");
        //System.out.println();
        alert1.show();
        }
        } else if (tf1.getText().isEmpty()) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("出错了");
        alert1.headerTextProperty().set("输入内容为空");
        alert1.show();
        }
        });
        }
//替换方法
private void replace(){


        HBox box1 = new HBox();
        box1.setPadding(new Insets(10,10,10,10));
        Label label1 = new Label(("查找内容："));
        TextField tf1 = new TextField();
        box1.getChildren().addAll(label1,tf1);
        HBox box3 = new HBox();
        Label label2 = new Label(("替换内容："));
        label2.setPadding(new Insets(2 ,0,0,10));
        TextField tf2 = new TextField();
        box3.getChildren().addAll(label2,tf2);

        VBox box4 = new VBox();
        box4.getChildren().addAll(box1,box3);

        VBox box2 = new VBox();
        box2.setPadding(new Insets(13, 10, 0, 10));
        Button btn1 = new Button("查找下一个(F)");
        Button btn2 = new Button("替换");
        Button btn3 = new Button("取消");
        box2.getChildren().addAll(btn1,btn2,btn3);
        HBox replaceRootNode = new HBox();
        replaceRootNode.getChildren().addAll(box4,box2);

        Stage replace = new Stage();
        Scene scent = new Scene(replaceRootNode,400,100);
        replace.setTitle("替换");
        replace.setScene(scent);
        replace.setResizable(false);
        replace.show();

        //查找功能
        btn1.setOnAction((ActionEvent e) -> {
        String textString = textarea.getText(); // 获取记事本文本域的字符串
        String tfString = tf1.getText(); // 获取要查找的字符串
        //输入是否为空
        if (!tf1.getText().isEmpty()) {
        //包含
        if (textString.contains(tfString)) {
        // 查找方法
        index = textarea.getText().indexOf(tf1.getText(), index);
        if (index == -1) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("已经找不到相关内容了");
        alert1.show();
        }
        if (index >= 0 && index < textarea.getText().length()) {
        textarea.selectRange(index, index + tf1.getText().length());
        index += tf1.getText().length();
        }
        //替换功能
        btn2.setOnAction((ActionEvent e2) -> {
        if (tf2.getText().isEmpty()) {     //替换内容为空时
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("出错了");
        alert1.headerTextProperty().set("替换内容为空");
        alert1.show();
        } else {           //替换内容不为空则替换
        textarea.replaceSelection(tf2.getText());
        }
        });
        } else if (!textString.contains(tfString)) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("找不到相关内容了");
        alert1.show();
        }
        }
        else if (tf1.getText().isEmpty()) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("出错了");
        alert1.headerTextProperty().set("输入内容为空");
        alert1.show();
        }
        });
        //关闭替换窗口
        btn3.setOnAction((ActionEvent e)->{
        replace.close();
        });
        }
//更换大小写方法
private void change(){
        HBox box1 = new HBox();
        box1.setPadding(new Insets(10,10,10,10));
        Label label1 = new Label(("查找内容："));
        TextField tf1 = new TextField();
        box1.getChildren().addAll(label1,tf1);

        VBox box4 = new VBox();
        box4.getChildren().addAll(box1);

        VBox box2 = new VBox();
        box2.setPadding(new Insets(5, 10, 0, 10));
        Button btn1 = new Button("查找");
        Label a = new Label("  ");
        Label b = new Label("  ");
        Label c = new Label("  ");
        Button btn2 = new Button("转换成大写");
        Button btn3 = new Button("转换成小写");
        Button btn4 = new Button("取消");
        box2.getChildren().addAll(btn1,a,btn2,b,btn3,c,btn4);

        HBox changeRootNode = new HBox();
        changeRootNode.getChildren().addAll(box4,box2);

        Stage change = new Stage();
        Scene scene = new Scene(changeRootNode,400,150);
        change.setTitle("大小写替换");
        change.setScene(scene);
        change.show();

        //查找功能
        btn1.setOnAction((ActionEvent e) -> {
        String textString = textarea.getText(); // 获取记事本文本域的字符串
        String tfString = tf1.getText(); // 获取要查找的字符串
        //输入是否为空
        if (!tf1.getText().isEmpty()) {
        //包含
        if (textString.contains(tfString)) {
        // 查找方法
        index = textarea.getText().indexOf(tf1.getText(), index);
        if (index == -1) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("已经找不到相关内容了");
        alert1.show();
        }
        if (index >= 0 && index < textarea.getText().length()) {
        textarea.selectRange(index, index + tf1.getText().length());
        index += tf1.getText().length();
        }
        //替换功能
        btn2.setOnAction((ActionEvent e2) -> {//转换成大写

        if (index == -1) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("已经找不到相关内容了");
        alert1.show();
        }
        else{
        String timesave = new String(tf1.getText());
        String big =  timesave.toUpperCase();
        textarea.replaceSelection(big);
        index = textarea.getText().indexOf(tf1.getText(), index);
        }
        });
        btn3.setOnAction((ActionEvent e2)->{//转换成小写
        if (index == -1) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("已经找不到相关内容了");
        alert1.show();
        }
        else{
        String timesave = new String(tf1.getText());
        String small = timesave.toLowerCase();
        textarea.replaceSelection(small);
        index = textarea.getText().indexOf(tf1.getText(), index);
        }
        });
        } else if (!textString.contains(tfString)) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("提示");
        alert1.headerTextProperty().set("找不到相关内容了");
        alert1.show();
        }
        }
        else if (tf1.getText().isEmpty()) {
        Alert alert1 = new Alert(AlertType.WARNING);
        alert1.titleProperty().set("出错了");
        alert1.headerTextProperty().set("输入内容为空");
        alert1.show();
        }
        });
        //关闭替换窗口
        btn4.setOnAction((ActionEvent e)->{
        change.close();
        });
        }
//color对象转换成十六进制返回
private  String choosecolor(Color color){
        String R = Integer.toHexString((int)(color.getRed()*255));
        R = R.length() < 2?('0'+R) : R;
        String B = Integer.toHexString((int)(color.getGreen()*255));
        R = R.length() < 2?('0'+R) : R;
        String G = Integer.toHexString((int)(color.getBlue()*255));
        R = R.length() < 2?('0'+R) : R;
        return "#"+R+B+G;
    }
    }

