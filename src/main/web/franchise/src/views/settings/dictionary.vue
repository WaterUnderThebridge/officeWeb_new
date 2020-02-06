<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-col :span="4">
        <el-select clearable  style="width: 130px" v-model="listQuery.table" :placeholder="$t('filter.table')">
          <el-option v-for="item in  [{label:'渠道',key:'channel'}]" :key="item.key" :label="item.label" :value="item.key">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-input @keyup.enter.native="handleFilter" style="width: 300px;" :placeholder="$t('filter.content')" v-model="listQuery.content">
        </el-input>
      </el-col>
      <el-col :span="3">
        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('filter.search')}}</el-button>
      </el-col>
      <el-col :span="3">
        <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">{{$t('table.add')}}</el-button>
      </el-col>
    </div>

    <el-table size="small" :key='tableKey' :data="listFilter" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 100%">
        <el-table-column fixed type="index" width="50"></el-table-column>
        <el-table-column v-for="item of header" :key="item.key" align="left" :sortable="is_sort(item.key)" :width="adjust_w(item.key)"  :label="item.label">
          <template slot-scope="scope">
              <span v-html="scope.row[item.key]"></span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('table.actions')" width="300" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="primary" @click="handleUpdate(scope.row)">{{$t('table.edit')}}</el-button>
            <el-button  type="danger" @click="handleModifyStatus(scope.row,'deleted')">{{$t('table.delete')}}
            </el-button>
          </template>
        </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="70px" style='width: 400px; margin-left:50px;'>
        <el-form-item :label="$t('table.definition')" prop="name">
          <el-input v-model="temp.name"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="comfirmUpdate">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>

 

  </div>
</template>

<script>
import { dicList, dicAdd, dicDel, dicUpdate } from '@/api/dictionary'
import waves from '@/directive/waves' // 水波纹指令
import { parseTime } from '@/utils'

 

export default {
  name: 'dictionary',
  directives: {
    waves
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        content: undefined,
        table: 'channel',
        sort: '+id'
      },
      match:{
             name:{channel:"渠道名称"},
             id:{channel:"记录ID"}
             },
      header:[],
      temp: {
        id: undefined,
        name: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blus' }]
      },
      downloadLoading: false
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  created() {
    this.getList()
  },
  computed:{
     listFilter(){
        let offset =(this.listQuery.page-1)==0?0:(this.listQuery.page-1)*this.listQuery.limit;
        let data=[];
        this.list=this.list.filter((l)=>{return l.isdelete==0});
        for(var i=0;i<(this.listQuery.limit);i++){
             if(i+offset<this.total){
                data.push(this.list[i+offset]);
             }
        }
        return data;
    }
  },
  methods: {
    upper(i){ 
        return  typeof i =='string'?i.toUpperCase():i;
    },
    getList() {
      this.listLoading = true
      this.list=[],this.header=[],self=this;
      dicList(this.listQuery).then(response => {
        if(response.code==0){
           let res=response.data;
           if(res.length>0){
             this.list = res;
             this.total = res&&res.length;
             for(var i in res[0]){
                if(self.match[i]&&self.match[i]['channel']) 
                self.header.push({key:i,label:self.match[i]&&self.match[i]['channel']});
             }
           }
           console.log(this.header)
        }
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getList()
    },
    handleModifyStatus(row, status) {
      let self=this,msg,fn;
      if(status=='deleted') {
         msg='删除后不能恢复，是否确认删除?'
      }
      fn=self.dicDel;
      self.$confirm(msg)
        .then(function() {
          fn(row);
        })
        .catch(action => {
              if(action === 'cancel'){
                  self.$message({
                    type: 'info',
                    message: "已取消操作"
                  })
              }else{
                self.dicDel();
              }
              self.dialogFormVisible=false;
          }); 
    },
    dicDel(row){
        dicDel({table:this.listQuery.table,id:row['id']}).then((res)=>{
          if(res.code==0){
              this.$message({
                message: '操作成功',
                type: 'success'
              })
          }
          let indx=this.list.findIndex(function(l){
              return row['id']==l['id']
          })
          this.list.splice(indx,1);
        })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          dicAdd({table:this.listQuery.table,name:this.temp.name}).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.listQuery.page=1;
            this.getList();
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      //console.log(this.temp);
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    comfirmUpdate(){
      let self=this;
      self.$confirm('修改后，相关记录将会更新！是否确认修改?')
        .then(function() {
          self.updateData();
        })
        .catch(action => {
              if(action === 'cancel'){
                  self.$message({
                    type: 'info',
                    message: "已取消操作"
                  })
              }
              self.dialogFormVisible=false;
          }); 
    },
    updateData() {
      let self=this;
      self.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, self.temp)
          const query = Object.assign({}, self.temp);
          query.table=this.listQuery.table;
          dicUpdate(query).then((res) => {
            if(res.code==0){
                for (const v of self.list) {
                  if (v.id === self.temp.id) {
                    const index = self.list.indexOf(v)
                    self.list.splice(index, 1, self.temp)
                    break
                  }
                }
                self.dialogFormVisible = false
                self.$notify({
                  title: '成功',
                  message: '更新成功',
                  type: 'success',
                  duration: 2000
                })
            }
          })
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
      const index = this.list.indexOf(row)
      this.list.splice(index, 1)
    },
		is_sort:function(key){
        switch (key){
          default:
            return false;
        }
    },
    adjust_w:function(key){
      console.log(key)
          switch (key){
              case 'name':
                return null;
              default:
                return null;
          }
		  },
  }
}
</script>
