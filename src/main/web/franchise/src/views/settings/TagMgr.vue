<template>
    <el-container style="height: 400px; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-active="group_cur" @select="switchMenu">
            <el-submenu index="top">
                <template slot="title"><span>标签分组</span></template>
                <el-menu-item :key="key" :index="key+''" v-for="(m,key) of labelGrps"><i class="el-icon-message"></i>
                   <span>{{m.name}}</span>
                </el-menu-item>
            </el-submenu>
        </el-menu>
      </el-aside>
    <el-container>
    <el-header class="header" style="text-align: right; font-size: 12px;position:relative">
      <el-button size="mini" style="position:absolute;left:2%;top:30%" @click="tagAdd">添加标签</el-button>
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
    
    <el-main v-if="labelGrps&&labelGrps.length>0">
 

    <el-tag
        :key="index" 
        v-for="(value,index) of labelGrps[group_cur].tags"
        size="medium" 
        :type="Type[index%4]"
        closable
        :disable-transitions="false"
        @close="tagClose(index)">
        {{value.name}}
    </el-tag>

    </el-main>
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
import { sync } from '@/api/settings'

  export default {
    data:function() {
      const item = {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      };
      return {
        dialogFormVisible:false,
        labelGrps:undefined,
        // labelGrps:[{name:"例子来源",checked:true,tags:
        //   [
        //     {name:'phone in',iscolor:false},
        //     {name:'web in',iscolor:false},
        //     {name:'商场活动',iscolor:false},
        //     {name:'转介绍',iscolor:false},
        //     {name:'它中心转入',iscolor:false},
        //     {name:'walk in',iscolor:false},
        //     {name:'小区地推',iscolor:false},
        //   ]},{name:"电话状态",checked:true,tags:
        //   [
        //     {name:'暂时未接通',iscolor:false},
        //     {name:'暂时无人接听',iscolor:false},
        //     {name:'挂断',iscolor:false},
        //     {name:'已深入沟通',iscolor:false},
        //     {name:'仅粗略沟通',iscolor:false},
        //     {name:'尚未破冰',iscolor:false},
        //     {name:'破冰后略沟通',iscolor:false},
        //   ]},{name:"消费能力",checked:true,tags:
        //   [
        //     {name:"消费力强",iscolor:false},
        //     {name:"消费力弱",iscolor:false},
        //   ]},{name:"家长角色",checked:true,tags:
        //   [
        //     {name:"独立决策者",iscolor:false},
        //     {name:"决策干预者",iscolor:false},
        //     {name:"重要人物",iscolor:false}
        //   ]}],
        group_cur:"0",
        Type:["success","info","warning","danger"],
        tableData: Array(20).fill(item),
        form:{type:"",value:""}
      }
    },
    
    methods:{
      switchMenu(index, indexPath){
         this.group_cur=index;
      },
      tagClose(i){
        this.labelGrps[this.group_cur].tags.splice(i, 1);
      },
      tagAdd(){
         this.form={title:"添加标签",value:""};
         this.dialogFormVisible=true;
      },
      groupEdit(){
          this.form={title:"修改分组",value:this.labelGrps[this.group_cur].name};
          this.dialogFormVisible=true;
      },
      groupADD(){
           this.form={title:"添加分组",value:""};
           this.dialogFormVisible=true;
      },
      groupDel(){
           this.labelGrps.splice(this.group_cur,1);
      },
      save(){
          var val = this.form.value.trim();
          if(this.form.title=="添加分组"){
              if(this.tag_find(val,this.labelGrps)==-1){
                 this.labelGrps.unshift({name:val,checked:true,tags:[]});
              }else{
                 this.$message({message:'分组已存在，添加失败！',type: 'error'});
              }
          }else if(this.form.title=="修改分组"){
              this.labelGrps[this.group_cur].name=val;
          }else{
            //console.error(this.labelGrps[this.group_cur].tags);
              if(this.tag_find(val,this.labelGrps[this.group_cur].tags)==-1){
                 this.labelGrps[this.group_cur].tags.push({name:val,iscolor:false});
              }else{
                 this.$message({message:'标签已存在，请勿重复添加！',type: 'error'});  
              }
          }
          //关闭弹窗
          this.dialogFormVisible=false;
         // this.sync();
      },
      async sync(){
          let  res= await sync(this);
          console.log(res)
      }
    },
	 	watch:{
        labelGrps:{
            handler(newValue, oldValue) {
              console.log(oldValue)
              //if(!oldValue) this.sync();
        　　},
    　　　  deep: true
        },
        account:function(){
          this.get_tag();
        }
    },
    mounted(){
       this.get_tag();
    }
  };
</script>
