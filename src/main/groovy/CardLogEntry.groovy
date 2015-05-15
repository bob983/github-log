import groovy.transform.Sortable
import groovy.transform.ToString

import java.time.ZonedDateTime

@ToString
@Sortable(includes = ['commitDate'])
class CardLogEntry {
    int id
    Card card
    String authorEmail
    String fullMessage
    String commitId
    ZonedDateTime commitDate
}
