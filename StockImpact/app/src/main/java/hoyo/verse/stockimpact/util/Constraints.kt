package hoyo.verse.stockimpact.util

//1: "Blockchain",
//    2: "Semiconductor",
//    3: "Biotechnology",
//    4: "Artificial Intelligence",
//    5: "Vehicles"

class Constraints {
    companion object {
        const val BLOCKCHAIN = 1
        const val SEMICONDUCTOR = 2
        const val BIOTECHNOLOGY = 3
        const val AI = 4
        const val VEHICLES = 5

        fun convertNumToTopics(topic: Int): String{
            if (topic == 1)
                return "Blockchain"
            else if (topic == 2)
                return "Semiconductor"
            else if (topic == 3)
                return "Biotechnology"
            else if (topic == 4)
                return "Artificial Intelligence"
            else if (topic == 5)
                return "Vehicle"
            else
                return ""
        }
    }
}