package com.example.xstream.whitelisting.inconsistency;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import java.awt.Color;
import java.awt.Font;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ClassWhitelistingTest
{
    private static final XStream XSTREAM = createXstreamForSerialization();

    private static XStream createXstreamForSerialization()
    {
        XStream xstream = new XStream();
        //xstream.alias("ImageParameters", ImageParameters.class);
        return xstream;
    }
    
    private static ImageParameters createImageParameters() {
        ImageParameters imageParameters = new ImageParameters();
        imageParameters.setImage(new byte[] {0, 1, 2, 3, 4, 5, 6}); // not really an image, just for testing
        imageParameters.setX(10);
        imageParameters.setY(20);
        
		TextParameters textParameters = new TextParameters();
		textParameters.setText("Some text...\r\nSome more text...");
		textParameters.setTextColor(Color.BLACK);
        textParameters.setFont(new Font("Arial", Font.PLAIN, 8));
		imageParameters.setTextParameters(textParameters);
        
        return imageParameters;
    }
    
    private static XStream createXstreamDeserializerForImageParameters()
    {
        XStream xstream = createBaseWhitelistingDeserializer();
        
        //xstream.alias("ImageParameters", ImageParameters.class);
        // Note: for some reasons Font and Color do not need to be mentioned, even though they are used (is that due to default ColorConverter and FontConverter?)
        // Note: for some reasons ImageTextParameters needs not to be mentioned either. However, without String it fails.
        xstream.allowTypes(new Class[]{ImageParameters.class /*String.class, byte[].class, TextParameters.class, Color.class, Font.class*/ });
        
        return xstream;
    }

    private static XStream createBaseWhitelistingDeserializer()
    {
        XStream xstream = new XStream();
        // Add additional security by whitelisting allowed classes (as suggested on http://xstream.codehaus.org/security.html)
        // clear out existing permissions and set own ones
        xstream.addPermission(NoTypePermission.NONE);
        // allow some basics
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        return xstream;
    }

        // deserialization will fail because String.class was not whitelisted which is used by TextParameters with (but it only happens if Font needs to be deserialized, not when just String needs to be deserialized):
        /*
testDeserializationWithoutStringInAllowTypes(com.example.xstream.whitelisting.inconsistency.ClassWhitelistingTest)  Time elapsed: 0.266 sec  <<< ERROR!
com.thoughtworks.xstream.converters.ConversionException: java.lang.String : java.lang.String
---- Debugging information ----
message             : java.lang.String
cause-exception     : com.thoughtworks.xstream.security.ForbiddenClassException
cause-message       : java.lang.String
class               : java.awt.Font
required-type       : java.awt.Font
converter-type      : com.thoughtworks.xstream.converters.extended.FontConverter
path                : /ImageParameters/textParameters/font/family
line number         : 10
class[1]            : com.example.xstream.whitelisting.inconsistency.TextParameters
converter-type[1]   : com.thoughtworks.xstream.converters.reflection.ReflectionConverter
class[2]            : com.example.xstream.whitelisting.inconsistency.ImageParameters
version             : 1.4.7
-------------------------------
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convert(TreeUnmarshaller.java:79)
	at com.thoughtworks.xstream.core.AbstractReferenceUnmarshaller.convert(AbstractReferenceUnmarshaller.java:65)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convertAnother(TreeUnmarshaller.java:66)
	at com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.unmarshallField(AbstractReflectionConverter.java:474)
	at com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.doUnmarshal(AbstractReflectionConverter.java:406)
	at com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.unmarshal(AbstractReflectionConverter.java:257)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convert(TreeUnmarshaller.java:72)
	at com.thoughtworks.xstream.core.AbstractReferenceUnmarshaller.convert(AbstractReferenceUnmarshaller.java:65)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convertAnother(TreeUnmarshaller.java:66)
	at com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.unmarshallField(AbstractReflectionConverter.java:474)
	at com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.doUnmarshal(AbstractReflectionConverter.java:406)
	at com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.unmarshal(AbstractReflectionConverter.java:257)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convert(TreeUnmarshaller.java:72)
	at com.thoughtworks.xstream.core.AbstractReferenceUnmarshaller.convert(AbstractReferenceUnmarshaller.java:65)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convertAnother(TreeUnmarshaller.java:66)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.convertAnother(TreeUnmarshaller.java:50)
	at com.thoughtworks.xstream.core.TreeUnmarshaller.start(TreeUnmarshaller.java:134)
	at com.thoughtworks.xstream.core.AbstractTreeMarshallingStrategy.unmarshal(AbstractTreeMarshallingStrategy.java:32)
	at com.thoughtworks.xstream.XStream.unmarshal(XStream.java:1185)
	at com.thoughtworks.xstream.XStream.unmarshal(XStream.java:1169)
	at com.thoughtworks.xstream.XStream.fromXML(XStream.java:1040)
	at com.thoughtworks.xstream.XStream.fromXML(XStream.java:1031)
	at com.example.xstream.whitelisting.inconsistency.ClassWhitelistingTest.testDeserializationWithoutStringInAllowTypes(ClassWhitelistingTest.java:82)
        */
    @Test
    public void testDeserializationWithoutStringInAllowTypes()
    {
        ImageParameters imageParameters = createImageParameters();
        String imageParametersSerialized = XSTREAM.toXML(imageParameters);

        XStream xstream = createXstreamDeserializerForImageParameters();
        ImageParameters imageParametersDeserialized = (ImageParameters)xstream.fromXML(imageParametersSerialized);
        Assert.assertNotNull(imageParametersDeserialized);
    }
    
    
    // passes, even though some of the types are not mentioned
    @Test
    public void testDeserializationWithStringInAllowTypes()
    {
        ImageParameters imageParameters = createImageParameters();
        String imageParametersSerialized = XSTREAM.toXML(imageParameters);
        
        XStream xstream = createBaseWhitelistingDeserializer();
        //xstream.alias("ImageParameters", ImageParameters.class);
        // Note: for some reasons Font and Color do not need to be mentioned, even though they are used (is that due to default ColorConverter and FontConverter?)
        // Note: for some reasons ImageTextParameters needs not to be mentioned either.
        xstream.allowTypes(new Class[]{String.class, /*byte[].class,*/ ImageParameters.class/*, TextParameters.class, Color.class, Font.class*/});
        ImageParameters imageParametersDeserialized = (ImageParameters)xstream.fromXML(imageParametersSerialized);
        Assert.assertNotNull(imageParametersDeserialized);
    }
    
    // OK, String is not a problem when used in a custom class
    @Test
    public void testDeserializationWithoutStringInAllowTypesButNoFont()
    {
        StringWrapper stringWrapper = new StringWrapper();
        stringWrapper.setStringValue("Test");
        String stringWrapperSerialized = XSTREAM.toXML(stringWrapper);
        
        XStream xstream = createBaseWhitelistingDeserializer();
        xstream.allowTypes(new Class[]{StringWrapper.class});
        StringWrapper stringWrapperDeserialized = (StringWrapper)xstream.fromXML(stringWrapperSerialized);
        Assert.assertNotNull(stringWrapperDeserialized);
        Assert.assertEquals("Test", stringWrapper.getStringValue());
    }
    
    // fails, String is not whitelisted
    @Test
    public void testFontDeserializationWithoutStringInAllowTypes()
    {
        Font font = new Font("Arial", Font.PLAIN, 8);
        String fontSerialized = XSTREAM.toXML(font);
        
        XStream xstream = createBaseWhitelistingDeserializer();
        xstream.allowTypes(new Class[]{Font.class});
        Font fontDeserialized = (Font)xstream.fromXML(fontSerialized);
        
        Assert.assertNotNull(fontDeserialized);
        Assert.assertEquals("Arial", fontDeserialized.getFontName());
        Assert.assertEquals(Font.PLAIN, fontDeserialized.getStyle());
        Assert.assertEquals(8, fontDeserialized.getSize());
    }
    
    // font deserialization succeeds only when String whitelisted
    @Test
    public void testFontDeserializationWithStringInAllowTypes()
    {
        Font font = new Font("Arial", Font.PLAIN, 8);
        String fontSerialized = XSTREAM.toXML(font);
        
        XStream xstream = createBaseWhitelistingDeserializer();
        xstream.allowTypes(new Class[]{String.class, Font.class});
        Font fontDeserialized = (Font)xstream.fromXML(fontSerialized);
        
        Assert.assertNotNull(fontDeserialized);
        Assert.assertEquals("Arial", fontDeserialized.getFontName());
        Assert.assertEquals(Font.PLAIN, fontDeserialized.getStyle());
        Assert.assertEquals(8, fontDeserialized.getSize());
    }
}
