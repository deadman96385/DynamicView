DynamicView
===========

A library that supports dynamic parsing of XML layouts. With this library, you can dynamically render a layout similar to the following. DynamicView caches the view structure without repeatedly parsing the XML. <br>
The root node SN attribute must be set. Different layouts must be different. The view structure cache is based on this attribute.

```xml
<RBox sn='000001'>
    <VBox background='#80E0E0E0 20 20 0 0' padding='18 18 18 10' margin='14'>
        <Text text='{title}' fontSize='20' color='black'/>
        <Text text='金额' margin='0 10 0 0'/>
        <RBox>
            <Text name='money' text='{money}' fontSize='28' color='black'/>
            <Text rightOf='@money' alignBaseline='@money' text='元'/>
        </RBox>
        <Grid spanCount='2' dataSource='{items}'>
            <VBox margin='0 10 0 0'><Text text='{name}'/>
                <Text text='{value}' color='black'/>
            </VBox>
        </Grid>
    </VBox>
</RBox>
```

## Component Description

### Node Description

The name of the node is the class name of the custom control we need, the property is the setter of the defined class, for example, Text will have the following definition.

```java
@DynamicView
public class Text extends TextView implements ViewType.View {
    public Text(Context context) {
        super(context);
    }

    public void setText(String text) {
        super.setText(text);
    }

    public void setFontSize(String value) {
        setTextSize(Integer.parseInt(value));
    }

    public void setColor(String value) {
        setTextColor(Color.parseColor(value));
    }

    public void setStyle(String value) {
        switch (value) {
            case "bold":
                setTypeface(getTypeface(), Typeface.BOLD);
                break;
            case "italic":
                setTypeface(getTypeface(), Typeface.ITALIC);
                break;
        }
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }
}
```
The DynamicView annotation is to indicate that this class is a node control, and the corresponding helper class is automatically generated at compile time.

There are three types of ViewType
* View - indicates that this node is a View and cannot contain child nodes
* GroupView - indicates that this node is a container, can contain child nodes, refer to LinearLayout, RelativeLayout, etc.
* AdapterView - This node can only have one child node. According to the data, how many child views are generated, refer to ListView, RecyclerView, etc.

Property values have two forms
* Literal value title="Hello World", this property will be set directly to the control via setter
* Dynamic attribute logo="{logo}" logo will be bound to a Map or JSONObject key is the value of the logo

### Built-in node

* HBox horizontal layout container, LinearLayout Orientation is Horizontal
* VBox vertical layout container, LinearLayout Orientation is Vertical
* RBox relative layout container, using RelativeLayout
* Grid Adapter layout container, using RecyclerView
* Text text control
* Image Picture Control
### Attribute Description

General property
<table>
<tr><th>Name</th><th> format (N for numeric values, S for string)</th><th> Description (unless otherwise stated, the unit of N is dp)</th>< /tr>
<tr><td>name</td><td>S</td><td>Control ID</td></tr>
<tr><td>size</td><td nowrap>match|wrap|N [match|wrap|N]</td><td>Set width and height, if there is only one, then width and height are the same </ Td></tr>
<tr><td>margin</td><td> N [N] [N] [N] </td><td> Top left and bottom right, one time all the same, two hours left and right, up and down</td> </tr>
<tr><td>padding</td><td> N [N] [N] [N] </td><td> Same as above</td></tr>
<tr><td>background</td><td> N(color) [N] [N] [N] [N] </td><td> The first is the background color and the last four are the rounded corners Radius, top left, top right, bottom right, bottom left</td></tr>
<tr><td>gravity</td><td>center | left | right | top | bottom</td><td> single or combined, separated by |</td></tr>
<tr><td>weight</td><td>N</td><td>Only the control settings placed in HBox and VBox will have an effect</td></tr>
<tr><td>leftOf</td><td>@S</td><td>S is the control identifier set by name, only the control settings placed in RBox will have effect</td></ Tr>
<tr><td>rightOf</td><td>@S</td><td>ibid</td></tr>
<tr><td>above</td><td>@S</td><td>ibid</td></tr>
<tr><td>below</td><td>@S</td><td>ibid</td></tr>
<tr><td>alignLeft</td><td>@S</td><td>ibid</td></tr>
<tr><td>alignRight</td><td>@S</td><td>ibid</td></tr>
<tr><td>alignTop</td><td>@S</td><td>ibid</td></tr>
<tr><td>alignBottom</td><td>@S</td><td>ibid</td></tr>
<tr><td>alignBaseline</td><td>@S</td><td>ibid</td></tr>
<tr><td>align</td><td>center | left | right | top | bottom</td><td> single or combination, separated by \|, <font color=#A52A2A> container class and Text Has this property, which controls the alignment of child elements</td></tr>
</table>

 Text
<table>
<tr><th>Name </th><th> Format (N for numeric values, S for strings) </th><th> Description </th></tr>
<tr><td>text</td><td> S </td><td>Text Content</td></tr>
<tr><td>fontSize</td><td> N </td><td>font size in sp</td></tr>
<tr><td>color</td><td> N(color) </td><td> font color</td></tr>
<tr><td>style</td><td> bold | italic</td><td> Set bold or italic</td></tr>
</table>

 Image
<table>
<tr><th>Name </th><th> Format (N for numeric values, S for strings) </th><th> Description </th></tr>
<tr><td>src</td><td>res://xxxx | url | path </td><td> resource name, URL, local path</td></tr>
<tr><td>scale</td><td>stretch | fitStart | fitEnd | fitCenter | center | centerCrop | centerInside </td><td>Set the zoom type</td></tr>
</table>

Grid
<table>
<tr><th>Name </th><th> Format (N for numeric values, S for strings) </th><th> Description </th></tr>
<tr><td>dataSource</td><td> S(JSONArray) </td><td> Content Array</td></tr>
<tr><td>spanCount</td><td> N </td><td> number of columns</td></tr>
</table>

### Grid Node

```xml
<Grid spanCount='2' dataSource='{items}'>
    <VBox margin='0 10 0 0'><Text text='{name}'/>
        <Text text='{value}' color='black'/>
    </VBox>
</Grid>
```
The Grid node can only have one child node. This node can be understood as a template of the child control. The Grid dynamically generates the corresponding child View according to the number of items array. Each value in the items array is a JSONObject, and the dynamic properties in the child nodes are bound to this JSONObject.

### Event processing

```java
// event handler
Public interface ActionProcessor {
     /**
     * view event produces a view
     * tag event tag
     * data additional parameters
     */
     Void processAction(View view, String tag, Object... data);
}
```

The format of the event attribute is (tag) or ({value}), which uses the value of the key in the bound data as the value as the tag.
```xml
<Text text='{value}' onClick='(VALUE_CLICK)' color='black'/>
<Text text='{value}' onClick='({VALUE_CLICK})' color='black'/>
```

General event
<table>
<tr><th>Name </th><th> Description </th></tr>
<tr><td>onClick</td><td>Control click event</td></tr>
</table>


### Instructions

```gradle
compile 'com.benny.library:dynamicview:0.0.3'
annotationProcessor 'com.benny.library:dynamicview-compiler:0.0.3'
```
```java

/ / Create a View, the first parameter is Context, the second is a string containing xml
View convertView = DynamicViewEngine.getInstance().inflate(context, parent, layoutXml);
// Register event handler
DynamicViewEngine.setActionProcessor(convertView, new ActionProcessor() {
     @Override
     Public void processAction(View view, String tag, Object... data) {
         //xxxxx
     }
});
// Bind dynamic property, the first parameter is the view created by the above method, the second value is data, Map<String, String> or JSONObject
DynamicViewEngine.getInstance().bindView(convertView, data);

```

## Discussion

### QQ Group: 516157585
