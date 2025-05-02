package vcmsa.projects.tcss.data

import android.widget.EditText
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(


    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var username: String,
    var email: String,
    var birthDate: String,
    var phone: String,
    var password: String,
)