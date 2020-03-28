//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2020.03.03 at 03:37:06 PM AST
//


package sa.tamkeentech.tbs.schemas.refund;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RefundNotifyRq_Type complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RefundNotifyRq_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RqUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Timestamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RefundNotificationRec" type="{http://www.BrightWare.com.sa/SADADWare}RefundNotificationRec_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefundNotifyRq_Type", propOrder = {
    "rqUID",
    "timestamp",
    "refundNotificationRec"
})
public class RefundNotifyRqType {

    @XmlElement(name = "RqUID")
    private String rqUID;
    @XmlElement(name = "Timestamp")
    private String timestamp;
    @XmlElement(name = "RefundNotificationRec")
    private List<RefundNotificationRecType> refundNotificationRec;

    /**
     * Gets the value of the rqUID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRqUID() {
        return rqUID;
    }

    /**
     * Sets the value of the rqUID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRqUID(String value) {
        this.rqUID = value;
    }

    /**
     * Gets the value of the timestamp property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the refundNotificationRec property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the refundNotificationRec property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRefundNotificationRec().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RefundNotificationRecType }
     *
     *
     */
    public List<RefundNotificationRecType> getRefundNotificationRec() {
        if (refundNotificationRec == null) {
            refundNotificationRec = new ArrayList<RefundNotificationRecType>();
        }
        return this.refundNotificationRec;
    }

}