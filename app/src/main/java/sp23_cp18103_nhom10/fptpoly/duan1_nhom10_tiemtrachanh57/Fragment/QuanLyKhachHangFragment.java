package sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.Adapter.AdapterQuanLyKhachHang;
import sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.Adapter.AdapterQuanLyNhanVien;
import sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.DAO.KhachHangDAO;
import sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.DTO.KhachHang;
import sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.DTO.NhanVien;
import sp23_cp18103_nhom10.fptpoly.duan1_nhom10_tiemtrachanh57.R;

public class QuanLyKhachHangFragment extends Fragment {
    ListView lvKhachHang;
    ArrayList<KhachHang> list;
    FloatingActionButton fabKhachHang;
    Dialog dialog;
    EditText edMaKhachHang, edHoTenKhachHang, edSDTKhachHang, edNamSinhKhachHang;
    RadioButton rdoNamKH, rdoNuKH;
    RadioGroup rdgGioiTinhKhạchHang;
    Button btnLuuKhachHang, btnHuyKhachHang;

    static KhachHangDAO dao;
    AdapterQuanLyKhachHang adapterQuanLyKhachHang;
    KhachHang item;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang, null);
        lvKhachHang = view.findViewById(R.id.lvKhachHang);
        fabKhachHang = view.findViewById(R.id.fabKhachHang);
        dao = new KhachHangDAO(getActivity());
        capNhapLV();


        fabKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(getActivity(),0);
            }
        });
        lvKhachHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = list.get(i);
                openDialog(getActivity(), 1);
                return false;
            }
        });
        return view;

    }
    public void openDialog(final Context context,final int type) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_khachhang);
        //dialog khach hàng

        edMaKhachHang = dialog.findViewById(R.id.edMaKhachHang);
        edHoTenKhachHang = dialog.findViewById(R.id.edHoTenKhachHang);
        edSDTKhachHang = dialog.findViewById(R.id.edSDTKhachHang);
        edNamSinhKhachHang = dialog.findViewById(R.id.edNamSinhKhachHang);

        btnLuuKhachHang = dialog.findViewById(R.id.btnLuuKhachHang);
        btnHuyKhachHang = dialog.findViewById(R.id.btnHuyKhachHang);

        rdoNamKH = dialog.findViewById(R.id.rdoNamKH);
        rdoNuKH = dialog.findViewById(R.id.rdoNuKH);

        rdgGioiTinhKhạchHang = dialog.findViewById(R.id.rdgGioiTinhKhạchHang);

        edMaKhachHang.setEnabled(false);

        if (type != 0) {
            edMaKhachHang.setText(item.getMaKH()+"");
            edHoTenKhachHang.setText(item.getHoTen());
            edSDTKhachHang.setText(item.getSdt());
            edNamSinhKhachHang.setText(item.getNamSinh());

            if (item.getGioiTinh() == 1) {
                rdoNamKH.setChecked(true);
            } else {
                rdoNuKH.setChecked(true);
            }



        }
        btnHuyKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnLuuKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = new KhachHang();
                item.setHoTen(edHoTenKhachHang.getText().toString());
                item.setSdt(edSDTKhachHang.getText().toString());
                item.setNamSinh(edNamSinhKhachHang.getText().toString());

                if (rdoNamKH.isChecked()) {
                    item.setGioiTinh(1);
                } else {
                    item.setGioiTinh(0);
                }

                if (validate() > 0) {
                    if (type == 0) {
                        if (dao.insertKhachHang(item) > 0) {
                            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        item.setMaKH(Integer.parseInt(edMaKhachHang.getText().toString()));
                        if (dao.updateKhachHang(item) > 0) {
                            Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Sửa không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                    capNhapLV();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }
    void capNhapLV() {
        list = (ArrayList<KhachHang>) dao.getAll();
        adapterQuanLyKhachHang = new AdapterQuanLyKhachHang(getActivity(), this, list);
        lvKhachHang.setAdapter(adapterQuanLyKhachHang);
    }
    public int validate(){
        int check = 1;
        return check;
    }
}
