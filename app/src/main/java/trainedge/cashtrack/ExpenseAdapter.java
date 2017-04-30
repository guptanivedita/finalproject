package trainedge.cashtrack;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import trainedge.cashtrack.models.ExpenseModel;

/**
 * Created by xaidi on 11-04-2017.
 */

class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {
    private final List<ExpenseModel> expenseList;

    //we also need a constructor for this example
    //alt insert to add constructor


    public ExpenseAdapter(List<ExpenseModel> expenseList) {
        this.expenseList = expenseList;
    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = ((LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.simple_expense_item, parent, false);
        return new ExpenseHolder(row);
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, final int position) {
        //databinding
        final ExpenseModel model = expenseList.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvAmt.setText(String.valueOf(model.getAmt()));
        holder.tvDate.setText(model.getDay() + "/" + model.getMonth() + "/" + model.getYear());
        holder.tvCategory.setText(model.getCategory());
        holder.cvCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()).setMessage("Do you want to delete " + model.getCategory() + " --> " + model.getAmt() + " ?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (new ExpenseDatabaseAdapter(v.getContext()).open().deleteExpense(model.getId()) > 0) {
                            notifyDataSetChanged();
                        }
                    }
                }).setNegativeButton("cancel", null);
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseHolder extends RecyclerView.ViewHolder {
        TextView tvAmt;
        TextView tvCategory;
        TextView tvTitle;
        TextView tvDate;
        CardView cvCard;

        public ExpenseHolder(View itemView) {
            super(itemView);
            cvCard = (CardView) itemView.findViewById(R.id.cvCard);
            tvAmt = (TextView) itemView.findViewById(R.id.tvAmt);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }
}
