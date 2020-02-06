<template>
  <div class="app-container">
      <div class="filter-container">
          <el-button v-if="isAdmin" type="primary" class="create" size="small"  @click="toDialog('create')">新建帐户</el-button>
      </div>
      <el-table :data="usersFilter" border fit highlight-current-row size="small" style="width:70%">
        <el-table-column prop="username" label="帐号名" width="140"></el-table-column>
        <el-table-column prop="fullname" label="姓名" width="140"></el-table-column>
        <el-table-column label="角色" >
          <template slot-scope="scope">
            {{scope.row.role}}
          </template>
        </el-table-column>
        <el-table-column align="left" :label="$t('table.actions')" width="250">
          <template slot-scope="scope">
            <el-col :span=6 :offset="1" v-if="isAdmin">
              <el-button type="primary" size="small" @click="toDialog('edit',scope.row)">{{$t('table.edit')}}</el-button>
            </el-col>
            <el-col :span=9>
              <el-button type="danger" size="small" @click="toDialog('reset',scope.row)">{{$t('table.resetPwd')}}</el-button>
            </el-col>
            <el-col :span=6 v-if="isAdmin">
              <el-button type="danger" size="small" @click="userDel(scope.row.id)">{{$t('table.delete')}}</el-button>
            </el-col>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :title="dialogTitle" :visible.sync="dialogUserVisible">
          <el-form :rules="userRules" ref="userForm" :model="user" label-position="center" >
                <el-row v-if="dialogTitle=='新建用户'">
                  <el-col :span="8">
                      <el-form-item label-width="80px" label="帐户名:" prop="username">
                        <el-input   style='min-width:150px;' v-model="user.username"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :offset="2" :span="8">
                      <el-form-item  label-width="80px" label="密码:" prop="password">
                        <el-input   style='min-width:150px;' v-model="user.password"></el-input>
                      </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                      <el-form-item  label-width="80px" label="姓名:" prop="fullname">
                            <el-input placeholder="" style='min-width:150px;' v-model="user.fullname"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :offset="2" :span="8">
                      <el-form-item label-width="80px" label="权限:"  prop="roleId">
                            <el-select v-model="user.roleId" placeholder="请选择">
                              <el-option
                                v-for="item in Roles"
                                :key="item.id"
                                :label="item.title"
                                :value="item.id">
                              </el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogUserVisible = false">{{$t('table.cancel')}}</el-button>
            <el-button type="primary" @click="userSave('userForm')">{{$t('table.save')}}</el-button>
          </div>
      </el-dialog>

      <el-dialog :title="dialogTitle" :visible.sync="dialogResetVisible" width="40%">
          <el-form label-position="center" ref="pwdForm" :rules="userRules" :model="user">
                <el-row>
                  <el-col :offset="3" :span="8">
                    <el-form-item label-width="80px" label="新密码:" prop="password">
                        <el-input   style='min-width:200px;' v-model="user.password"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogUserVisible = false">{{$t('table.cancel')}}</el-button>
            <el-button type="primary" @click="userSave('pwdForm')">{{$t('table.save')}}</el-button>
          </div>
      </el-dialog>
  </div>
</template>

<script>
import { roleList, userAdd, userDel,userUpdate} from '@/api/user' 
import { MessageBox, Message } from 'element-ui'
import { mapGetters } from 'vuex';

var validate_string = (rule, value, callback) => {
  if (!value) {
    callback(new Error(rule.message));
  }else{
    callback();
  } 
};
export default {
  data(){
    return {
      dialogUserVisible:false,
      dialogResetVisible:false,
      dialogTitle:"",
      Roles:[],
      users:[],
      user:{password:undefined,fullname:undefined,roleId:undefined,username:undefined},
    }
  },
  filters:{
     join(role){
       var roles=[];
       if(typeof role!="object") return "无";
       role.forEach(r => {
           roles.push(r.title);
       });
       return roles.join(",");
     }
  },
  computed:{
        ...mapGetters([
          'roles',
          'isAdmin',
          'isSuper',
          'userid'
        ]),
        usersFilter(){
          let self=this;
          return this.users.filter((u)=>{
            return u.isdelete==0
                  &&(u.id==self.userid
                  ||(self.isAdmin&&u.username!="test"&&u.username!="admin")
                  ||self.isSuper);
          })
        },
        userRules(){
          let rule={};
          let type=this.dialogTitle;
          if(type=='新建用户'){
              rule.username= [
                { validator: validate_string,message: '请输入用户名',trigger: 'blur' }
              ];
          }
          if(type=="新建用户"||type=="重置密码"){
              rule.password=[
                { required: true, message: '请输入密码', trigger: 'blur' },
                { min: 6, message: '至少6个字符', trigger: 'blur' }
              ]
          }
          if(type!="重置密码"){
              rule.fullname= [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                    { min: 2, message: '长度至少 2 个字符', trigger: 'blur' }
              ];
              rule.roleId=[
                    { required: true, message: '请选择权限', trigger: 'change' }
              ]
          }
          return rule;
      }
  },
  methods:{
    userDel(id){
      let self=this;
       userDel(id).then((response)=>{
          if(response.code==0){
              Message({
                message: response.msg,
                type: 'info',
                duration: 3 * 1000
              })
              self.getUsers();
          }
     });
    },
    userSave(form){
      let self=this;
      let save=this.user.id?userUpdate:userAdd;
       
      this.$refs[form].validate((valid)=> { 
         if(valid){
            save(this.user).then((response)=>{
                if(response.code==0){
                    Message({
                      message: response.msg,
                      type: 'info',
                      duration: 3 * 1000
                    })
                    self.getUsers();
                    self.dialogUserVisible=false;
                    self.dialogResetVisible=false;
                }
            });
          }
      })
    },
    toDialog(action,row){
       if(action=='reset'){
          this.user={};
          this.user.id=row.id;
          this.dialogTitle='重置密码'
          this.dialogResetVisible=true;
       }
       if(action=='create'){
          this.user={};
          this.dialogTitle='新建用户'
          this.dialogUserVisible=true;
       }
       if(action=='edit'){
          this.user=Object.assign({}, row) ;
          this.dialogTitle='修改用户信息'
          this.dialogUserVisible=true;
       }
       this.$nextTick(function(){
          for(var i in this.$refs){
              this.$refs[i].clearValidate();
         } 
       })
    }
  },
  mounted(){
     let self=this;
     this.getUsers();
     roleList().then((response)=>{
          if(response.code==0){
            self.Roles=response.data;
          }
     });
     
  },
  watch: {
  }
}
</script>

<style scoped>
  .create {
    margin-left:60%;
  }
</style>
