package com.my.dailyexpense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

   private List<GetExpense> expenseList;
   private Context context;
   private DatabaseHelper helper;
   private AlertDialog.Builder alert;

    public ExpenseAdapter(List<GetExpense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }




    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_expense,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, final int position) {

        final GetExpense exp = expenseList.get(position);

        holder.expensetype.setText(exp.getExp_type());
        holder.expensedate.setText(exp.getDate());
        holder.expenseamount.setText(String.valueOf(exp.getAmount()));
        holder.expensetime.setText(exp.getTime());
        holder.expenseId.setText(String.valueOf(exp.getId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(((FragmentActivity)context).getSupportFragmentManager(),"bottom");

                Bundle bundle = new Bundle();
                bundle.putString("type",exp.getExp_type());
                bundle.putString("amount",String.valueOf(exp.getAmount()));
                bundle.putString("date",exp.getDate());
                bundle.putString("time",exp.getTime());
                bundle.putString("image",exp.getImage());
                bottomSheet.setArguments(bundle);

            }
        });

       holder.menuId.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               PopupMenu popupMenu = new PopupMenu(context,holder.menuId);
               popupMenu.inflate(R.menu.options);
               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem menuItem) {
                       switch (menuItem.getItemId()){

                           case R.id.upadteId:

                               Intent intent = new Intent(context,Add_Expense.class);
                               intent.putExtra("id",String.valueOf(exp.getId()));
                               intent.putExtra("type",exp.getExp_type());
                               intent.putExtra("amount",String.valueOf(exp.getAmount()));
                               intent.putExtra("date",exp.getDate());
                               intent.putExtra("time",exp.getTime());
                               context.startActivity(intent);
                               break;
                           case R.id.deleteId:

                               alert = new AlertDialog.Builder(context);
                               alert.setTitle("Delete!");
                               alert.setMessage("Want to delete this expense??");
                               alert.setIcon(R.drawable.ic_warning_black_24dp);

                               alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                   }
                               });
                               alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       helper = new DatabaseHelper(context);
                                       helper.deleteData(exp.getId());
                                       expenseList.remove(position);
                                       notifyDataSetChanged();
                                       Toast.makeText(context, "Delete Successfull", Toast.LENGTH_SHORT).show();
                                   }
                               });
                               AlertDialog alertDialog = alert.create();
                               alertDialog.show();

                       }
                       return false;
                   }
               });
               popupMenu.show();
           }
       });


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView expensetype,expensedate,expenseamount,expensetime,expenseId;
        private ImageView menuId;
        public ViewHolder(View itemView) {
            super(itemView);

            expenseamount = itemView.findViewById(R.id.expenseamount);
            expensetype = itemView.findViewById(R.id.expensetype);
            expensedate = itemView.findViewById(R.id.expensedate);
            menuId = itemView.findViewById(R.id.menuId);
            expenseId= itemView.findViewById(R.id.expenseId);
            expensetime= itemView.findViewById(R.id.expensetime);

        }
    }

}
