package com.hui.iFrame.paper;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.hui.Dao.Dao;
import com.hui.javaBean.Item;
import com.hui.javaBean.TbGysInfo;

import com.hui.javaBean.TbKhInfo;


public class PaperModifyPanel extends JPanel {
	// 入库单号
	private JTextField materialId = new JTextField();
	// 入库时间
	private JTextField gmtCreated = new JTextField();
	// 客户编号
	private JTextField customerId = new JTextField();
	// 客户名称
	private JTextField name = new JTextField();
	// 电话/手机
	private JTextField mobile = new JTextField();
	// 纸张名称
	private JTextField paperName = new JTextField();
	// 来料规格
	private JTextField specification = new JTextField();
	// 来料数量
	private JTextField amount = new JTextField();
	// 备注
	private JTextField remark = new JTextField();
	// 签收人
	private JTextField userSigned = new JTextField();

	private JButton modifyBtn = new JButton("修改");

	private JButton delBtn = new JButton("删除");

	// 客户列表
	private JComboBox customers = new JComboBox();
	// 材料列表
	private JComboBox materials = new JComboBox();
	public PaperModifyPanel() {
		setLayout(new GridBagLayout());
		setBounds(10, 10, 510, 302);

		setupComponent(new JLabel("入库单号"), 0, 0, 1, 1, false);
		setupComponent(materialId, 1, 0, 3, 400, true);
		materialId.setEditable(false);

		setupComponent(new JLabel("入库时间"), 0, 1, 1, 1, false);
		setupComponent(gmtCreated, 1, 1, 3, 160, true);
		gmtCreated.setEditable(false);

		customers.setMaximumRowCount(5);
		setupComponent(customers, 1, 2, 3, 1, true);
		customers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doKeHuSelectAction();
			}
		});

		setupComponent(new JLabel("客户编号"), 0, 3, 1, 1, false);
		setupComponent(customerId, 1, 3, 1, 0, true);
		customerId.setEditable(false);

		setupComponent(new JLabel("客户名称"), 0, 4, 1, 1, false);
		setupComponent(name, 1, 4, 3, 0, true);
		name.setEditable(false);

		setupComponent(new JLabel("电话/手机"), 0, 5, 1, 1, false);
		setupComponent(mobile, 1, 5, 1, 0, true);
		mobile.setEditable(false);

		setupComponent(new JLabel("纸张名称"), 0, 6, 1, 1, false);
		setupComponent(paperName, 1, 6, 1, 0, true);

		setupComponent(new JLabel("来料规格"), 0, 7, 1, 1, false);
		setupComponent(specification, 1, 7, 1, 0, true);

		setupComponent(new JLabel("来料数量"), 0, 8, 1, 1, false);
		setupComponent(amount, 1, 8, 1, 0, true);

		setupComponent(new JLabel("备注"), 0, 9, 1, 1, false);
		setupComponent(remark, 1, 9, 1, 0, true);

		setupComponent(new JLabel("签收人"), 0, 10, 1, 1, false);
		setupComponent(userSigned, 1, 10, 1, 0, true);

		materials.setPreferredSize(new Dimension(230, 21));
		initComboBox();
		materials.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doGysSelectAction();
			}
		});
		setupComponent(materials, 1, 11, 2, 0, true);

		initialBtn();
	}

	private void initialBtn() {
		JPanel panel = new JPanel();
		panel.add(modifyBtn);
		panel.add(delBtn);

		setupComponent(panel, 3, 11, 1, 0, false);

		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item item = (Item) customers.getSelectedItem();
				if (item == null || !(item instanceof Item))
					return;
				int confirm = JOptionPane.showConfirmDialog(
						PaperModifyPanel.this, "确认修改？");
				if (confirm == JOptionPane.YES_OPTION) {
					int rs = Dao.delete("delete from tb_gysInfo where id='"
							+ item.getId() + "'");
					if (rs > 0) {
						JOptionPane.showMessageDialog(PaperModifyPanel.this,
								"材料" + item.getName() + "删除成功！");
						materials.removeItem(item);
					} else {
						JOptionPane.showMessageDialog(PaperModifyPanel.this,
								"材料" + item.getName() + "删除失败");
					}
				}
			}
		});

		modifyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item item = (Item) materials.getSelectedItem();
				TbGysInfo gysInfo = new TbGysInfo();
				gysInfo.setId(item.getId());
				gysInfo.setCustomerId(customerId.getText());
				gysInfo.setName(name.getText());
				gysInfo.setMobile(mobile.getText());
				gysInfo.setPaperName(paperName.getText());
				gysInfo.setSpecification(specification.getText());
				gysInfo.setAmount(amount.getText());
				gysInfo.setRemark(remark.getText());
				gysInfo.setUserSigned(userSigned.getText());
				if (Dao.updateGys(gysInfo) == 1) {
					JOptionPane.showMessageDialog(PaperModifyPanel.this, "更新成功");
				}
				else {
					JOptionPane.showMessageDialog(PaperModifyPanel.this, "更新失败");
				}
			}
		});
	}

	/**
	 * 选中客户信息
	 */
	private void doKeHuSelectAction() {
		Item selectedItem;
		if (!(customers.getSelectedItem() instanceof Item)) {
			return;
		}
		selectedItem = (Item) customers.getSelectedItem();
		TbKhInfo khInfo = Dao.getKhInfo(selectedItem);
		customerId.setText(khInfo.getId());
		name.setText(khInfo.getName());
		mobile.setText(khInfo.getMobile());
	}

	public void initComboBox() {
		List gysInfo = Dao.getGysInfos(null);
		List<Item> items = new ArrayList<Item>();
		materials.removeAllItems();
		for (Iterator iter = gysInfo.iterator(); iter.hasNext();) {
			List element = (List) iter.next();
			Item item = new Item();
			item.setId(element.get(0).toString().trim());
			item.setName(element.get(0).toString().trim() + ":" + element.get(2).toString().trim());
			if (items.contains(item))
				continue;
			items.add(item);
			materials.addItem(item);
		}
		doGysSelectAction();
	}

	private void setupComponent(JComponent component, int gridx, int gridy,
			int gridwidth, int ipadx, boolean fill) {
		final GridBagConstraints gridBagConstrains = new GridBagConstraints();
		gridBagConstrains.gridx = gridx;
		gridBagConstrains.gridy = gridy;
		if (gridwidth > 1)
			gridBagConstrains.gridwidth = gridwidth;
		if (ipadx > 0)
			gridBagConstrains.ipadx = ipadx;
		gridBagConstrains.insets = new Insets(5, 1, 3, 1);
		if (fill)
			gridBagConstrains.fill = GridBagConstraints.HORIZONTAL;
		add(component, gridBagConstrains);
	}

	private void doGysSelectAction() {
		Item selectedItem;
		if (!(materials.getSelectedItem() instanceof Item)) {
			return;
		}
		selectedItem = (Item) materials.getSelectedItem();
		TbGysInfo gysInfo = Dao.getGysInfo(selectedItem);
		materialId.setText(gysInfo.getId());
		gmtCreated.setText(gysInfo.getGmtCreated());
		customerId.setText(gysInfo.getCustomerId());
		name.setText(gysInfo.getName());
		mobile.setText(gysInfo.getMobile());
		paperName.setText(gysInfo.getPaperName());
		amount.setText(gysInfo.getAmount());
		specification.setText(gysInfo.getSpecification());
		remark.setText(gysInfo.getRemark());
		userSigned.setText(gysInfo.getUserSigned());
	}

	public void initCustomers() {
		List customerList = Dao.getKhInfos();
		List<Item> items = new ArrayList<Item>();
		customers.removeAllItems();
		for (Iterator iter = customerList.iterator(); iter.hasNext();) {
			List element = (List) iter.next();
			Item item = new Item();
			item.setId(element.get(0).toString().trim());
			item.setName(element.get(1).toString().trim());
			if (items.contains(item))
				continue;
			items.add(item);
			customers.addItem(item);
		}
	}
}