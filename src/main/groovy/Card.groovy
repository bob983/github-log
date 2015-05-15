import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable

@EqualsAndHashCode
@Sortable
class Card {

    public static String MISC = "Misc";

    public static Card DUMMY = new Card(board: MISC, cardNumber: "")

    String board
    String cardNumber

    @Override
    String toString() {
        if(board == MISC) {
            return ""
        } else {
            return "$board-$cardNumber"
        }
    }

    def markdowned() {
        if(board == MISC) {
            return ""
        } else {
            return "**$board-$cardNumber**"
        }
    }
}
