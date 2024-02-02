package com.mk.tool.stock;

import com.huaien.core.util.FileManager;
import com.mk.data.GetAllBankuaiCode;
import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.filter.GetNextZT;
import com.mk.tool.stock.model.KModel;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StragetyGroup {


    public static void main(String[] args) throws InterruptedException, IOException {
        StragetyZTBottom.main(args);
        StragetyBottom.main(args);
    }



}
