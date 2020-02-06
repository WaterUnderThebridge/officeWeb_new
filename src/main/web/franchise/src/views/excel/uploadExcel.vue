<template>
  <div class="app-container">
    <el-row>
      <el-col :span="16"><upload-excel-component @on-selected-file='selected'></upload-excel-component></el-col>
      <el-col :span="8"><el-button class="upload" type="primary" :loading="uploadLoading" v-waves icon="el-icon-upload2" @click="toUpload()">{{$t('excel.sync')}}</el-button></el-col>
    </el-row>
    
    <el-table :data="tableData" border v-loading="uploadLoading"  highlight-current-row style="width: 100%;margin-top:20px;">
      <el-table-column v-for='item of tableHeader' :prop="item" :label="item" :key='item'>
      </el-table-column>
    </el-table>
     <el-dialog title="选择字段映射关系" :visible.sync="dialogReflectVisible">
          <el-form  ref="reflectForm" label-position="center">
             <el-row v-for="(field,index) of tbModel" :key="index"> 
               <el-col :offset="4">
                  <el-form-item  label-width="100px" :label="field.label+' =>'">
                      <el-select v-model="field.key_in" value-key="key" placeholder="请选择" clearable>
                        <el-option 
                          v-for="(h,index) of tableHeaderFilter"
                          :key="index"
                          :label="h"
                          :value="h">
                        </el-option>
                      </el-select>
                  </el-form-item>
                </el-col>
             </el-row>
 
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogCientVisible = false">{{$t('table.cancel')}}</el-button>
            <el-button type="primary" @click="handleUpload()">{{$t('table.import')}}</el-button>
          </div>
      </el-dialog>
  </div>
</template>

<script>
import UploadExcelComponent from '@/components/UploadExcel/index.vue'
import waves from '@/directive/waves' // 水波纹指令
import {createClients}  from   '@/api/client'
import $ from 'jquery'
export default {
  name: 'uploadExcel',
  directives: {
    waves
  },
  components: { UploadExcelComponent },
  data() {
    return {
      tbModel:[{label:"申请人",key:"name",key_in:undefined},{label:"手机",key:"phone",key_in:undefined},{label:"邮箱",key:"email",key_in:undefined},{label:"申请区域",key:"address",key_in:undefined},{label:"投资金额",key:"amtInvest",key_in:undefined},{label:"申请日期",key:"createTime",key_in:undefined},{label:"来源渠道",key:"channel",key_in:undefined}],
      tableData: [],
      tableHeader: [],
      uploadLoading:false,
      dialogReflectVisible:false
    }
  },
  computed:{
    tableHeaderFilter(){
       let self=this;
       return self.tableHeader.filter((h)=>{
            return !self.tbModel.find(function(f){
                 return f.key_in==h;
            })
        })
    }
  },
  methods: {
    selected(data) {
      let self=this;
      this.tableData = data.results;
      this.tableHeader = data.header;
    },
    toUpload(){
      if(this.tableHeader.length==0){
        this.$message({
            showClose: true,
            message: "请先放入需要导入的Excel数据",
            type: 'error'
        });
        return;
      }
      if(this.tableData.length>8000){
        this.$message({
            showClose: true,
            message: "数据文件过大,建议不超过8000行",
            type: 'error'
        });
        return;
      }
      this.dialogReflectVisible=true;
    },
    handleUpload(){
      let columns=[],fields=[],self=this;
      this.tbModel.map(function(f){
          if(f.key_in){//有填选
            columns.push(f.key_in+" "+f.key);
            fields.push(f.key);
          }
      })
      if(columns.length==0){
        this.$message({
            showClose: true,
            message: "请选择映射关系",
            type: 'error'
        });
        return;
      }
  
      if(fields.indexOf("name")==-1||fields.indexOf("phone")==-1){
        this.$message({
            showClose: true,
            message: "申请人姓名或手机号,不能为空",
            type: 'error'
        });
        return;
      }

      let sql=this.json2Sql(this.tableData,(obj)=>{
            let  arr = []
            self.tableHeader.forEach((h)=>{
              if(h.indexOf('UNKNOWN')==-1&&h!=""){
                arr.push("'"+(obj[h]||'')+"' "+h)
              }
            })
            return arr;
      });
 
      sql="with tmp0 as("+sql+"),tmp as(select "+columns.join(",")+" from tmp0)"
      sql=sql+"insert into dbo.TLG_AffiliateInfo("+fields.join(",")+",search,remark)select tmp.*,tmp."+fields.join("+tmp.")+",'Excel 导入'+convert(varchar(20),getdate(),120) from tmp left join TLG_AffiliateInfo A on A.phone=tmp.phone and A.isdelete=0 where A.phone is null";
      console.log(sql)
      createClients(sql).then((res)=>{
         if(res.code==0){
            this.$message({
                showClose: true,
                message: '成功'+res.msg,
                type: 'success'
            });
            self.tbModel=[{label:"申请人",key:"name",key_in:undefined},{label:"手机",key:"phone",key_in:undefined},{label:"邮箱",key:"email",key_in:undefined},{label:"申请区域",key:"address",key_in:undefined},{label:"申请日期",key:"createTime",key_in:undefined},{label:"来源渠道",key:"channel",key_in:undefined}]
            self.dialogReflectVisible=false;
         }
      })
    }
  }
}
</script>
<style scoped>
   .upload{
     margin-top: 15%;
   }
</style>
