<template>
    <el-container style="height: 400px; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-active="group_cur" @select="switchMenu">
            <el-submenu index="top">
                <template slot="title"><span>客户分组</span></template>
                <el-menu-item :key="key" :index="key+''" v-for="(m,key) of clientlGrps"><i class="el-icon-message"></i>
                   <span>{{m.name}}</span>
                </el-menu-item>
            </el-submenu>
        </el-menu>
      </el-aside>
    <el-container>
    <el-header class="header" style="text-align: right; font-size: 12px;position:relative">
      <el-dropdown>
        <span class="el-dropdown-link">
             分组操作<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="groupADD">新增分组</el-dropdown-item>
          <el-dropdown-item @click.native="groupEdit" v-show="group_cur!='top'">修改分组</el-dropdown-item>
          <el-dropdown-item @click.native="groupDel">删除分组</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-header>
    

  </el-container>
      <el-dialog :title="form.title" :visible.sync="dialogFormVisible"  width="30%">
        <el-form>
          <el-form-item >
             <el-input v-model="form.value" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
    </el-dialog>
</el-container>
</template>
 

<style>
  .header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  .el-aside {
    color: #333;
  }
</style>

<script>
import { mapState } from 'vuex'
  export default {
     computed:{
      ...mapState([
        'account'
      ])
    },
    data:function() {
      const item = {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      };
      return {
        tableData: Array(20).fill(item),
        dialogFormVisible:false,
        clientlGrps:[],
        group_cur:"0",
        Type:["success","info","warning","danger"],
        form:{type:"",value:""}
      }
    },
    
    methods:{
      switchMenu(index, indexPath){
         this.group_cur=index;
      },
      groupEdit(){
          this.form={title:"修改分组",value:this.clientlGrps[this.group_cur].name};
          this.dialogFormVisible=true;
      },
      groupADD(){
           this.form={title:"添加分组",value:""};
           this.dialogFormVisible=true;
      },
      groupDel(){
           this.clientlGrps.splice(this.group_cur,1);
      },
      save(){
          var val = this.form.value.trim();
          if(this.form.title=="添加分组"){
              if(this.tag_find(val,this.clientlGrps)==-1){
                 this.clientlGrps.unshift({name:val});
              }else{
                 this.$message({message:'分组已存在，添加失败！',type: 'error'});
              }
          }else if(this.form.title=="修改分组"){
              this.clientlGrps[this.group_cur].name=val;
          }
          //关闭弹窗
          this.dialogFormVisible=false;
          this.sync();
      },
      sync(){
        var res = JSON.stringify(this.clientlGrps);
        var sql = "update yzx set crmzdy_82069677=quot;@resquot; from crm_zdytable_238592_26580_238592_view yzx where crmzdy_82004682=@gymcode;select 0 errcode for json path";
        sql = sql.replace("@res",res)
        sql=this.convertor.toUni(sql);
        this.$store.commit({
            type:"get_data",
            sql:sql,
            func:function(data){console.log(JSON.stringify(data))}
        })
      }
    },
	 	watch:{
		  	clientlGrps:function(){
			  	this.sync();
        },
        account:function(){
          this.get_clie_group();
        }
    },
    mounted(){
        this.get_clie_group();
    }
  };
</script>
