import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getgoing.GroupManager
import com.example.getgoing.R
import com.example.getgoing.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RespectiveBillDisplayAdapter(private val memberList: List<User>, private val eventName: String) :
    RecyclerView.Adapter<RespectiveBillDisplayAdapter.MemberViewHolder>() {
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bill_member_item_respective, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val currentItem = memberList[position]

        // Set user's name
        holder.memberName.text = currentItem.name

        // Set user's image - You need to handle loading images here
        holder.memberImage.setImageResource(currentItem.image!!)

        // Set input field for amount
        val groupID = GroupManager.currentGroup?.groupID
        mDbRef = FirebaseDatabase.getInstance().getReference().child("Bills").child(groupID!!)
            .child(eventName).child(currentItem.phone!!)
        Log.d("phone",currentItem.phone!!)
        mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val personalAmount = snapshot.getValue(Double::class.java).toString()
                holder.AmountCreateBill.text = "Amount $$personalAmount"

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun getItemCount() = memberList.size

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberName: TextView = itemView.findViewById(R.id.memberName)
        val memberImage: ImageView = itemView.findViewById(R.id.idMemberImage)
        val AmountCreateBill: TextView = itemView.findViewById(R.id.totalAmountBill)
    }
}
