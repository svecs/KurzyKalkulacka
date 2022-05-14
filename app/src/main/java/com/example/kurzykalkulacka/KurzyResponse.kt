package com.example.kurzykalkulacka

import androidx.navigation.Navigator
import org.simpleframework.xml.*
import java.util.*

@Root(name="Envelope", strict = false)
@Namespace(prefix = "gesmes", reference = "http://www.gesmes.org/xml/2002-08-01")
class KurzyResponse {

    class Sender {
        @get:Element(name = "name")
        @set:Element(name = "name")
        var name: String? = null
    }

    @Root(name = "Cube", strict = false)
    class CubeE {
        @get:Attribute(name = "currency")
        @set:Attribute(name = "currency")
        var currency: String? = null

        @get:Attribute(name = "rate")
        @set:Attribute(name = "rate")
        var rate: Double? = null
        override fun toString(): String {
            return "CubeE(currency=$currency, rate=$rate)"
        }
    }


    @Root(name = "Cube", strict = false)
    class CubeT {
        @get:Attribute(name = "time")
        @set:Attribute(name = "time")
        var time: String? = null

        @get:ElementList(name = "Cube", inline = true)
        @set:ElementList(name = "Cube", inline = true)
        @Path("Cube")
        var cubes: List<CubeE>? = null
        override fun toString(): String {
            return "CubeT(time=$time, cubes=$cubes)"
        }
    }

    /*@Element(name = "subject")
    var subject: String? = null*/

    /*@Element(name = "Sender")
    var sender: Sender? = null*/

    class CubeL {
        @get:ElementList(name = "Cube", inline = true)
        @set:ElementList(name = "Cube", inline = true)
        var cubes: List<CubeT>? = null

    }

    @get:Element(name = "Cube")
    @set:Element(name = "Cube")
    //@Path("Cube")
    var cube: CubeL? = null
    override fun toString(): String {
        return "KurzyResponse(cube=$cube)"
    }
}