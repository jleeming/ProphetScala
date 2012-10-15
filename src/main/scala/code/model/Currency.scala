package code.model
import java.util.Date
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.Extraction
import code.lib.serializer.BigDecimalSerializer

case class Currency(date: Date, value: BigDecimal)

object Currency {
    private implicit val formats =
        net.liftweb.json.DefaultFormats + BigDecimalSerializer
    /**
     * Convert the item to JSON format.  This is
     * implicit and in the companion object, so
     * an Currency can be returned easily from a JSON call
     */
    implicit def toJson(c: Currency): JValue =
        Extraction.decompose(c)
}