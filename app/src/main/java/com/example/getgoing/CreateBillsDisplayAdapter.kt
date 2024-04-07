import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getgoing.R
import com.example.getgoing.User

class CreateBillsDisplayAdapter(private val memberList: List<User>) : RecyclerView.Adapter<CreateBillsDisplayAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bill_member_item, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val currentItem = memberList[position]

        // Set user's name
        holder.memberName.text = currentItem.name

        // Set user's image - You need to handle loading images here

        holder.memberImage.setImageResource(currentItem.image!!)

        // Set input field for entering amount
        holder.inputAmountCreateBill.hint = "Amount ($)"
        // You may want to set a text watcher to listen for changes in the amount entered by the user
        // holder.inputAmountCreateBill.addTextChangedListener(...)
    }

    override fun getItemCount() = memberList.size
    fun getMemberList(): List<User> {
        return memberList
    }
    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberName: TextView = itemView.findViewById(R.id.memberName)
        val memberImage: ImageView = itemView.findViewById(R.id.idMemberImage)
        val inputAmountCreateBill: EditText = itemView.findViewById(R.id.inputAmountCreateBill)
        // You can add other views here if needed
    }
}