<template>
  <div class="app-container calendar-list-container">
     <el-row>
       <el-col :span="13">
          <el-col :span="15">
              <el-date-picker  
                  v-model="listQuery.dtzx"
                  :picker-options="rangeTimeOps"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="-"
                  type="daterange"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :default-time="['00:00:00', '23:59:59']"
                  align="left" clearable>
              </el-date-picker>
          </el-col>
          <el-col :span="8">
              <el-input style='margin-left:2px' :placeholder="search.placeholder" v-model="search.value" @keyup.enter.native="handleSearch" clearable></el-input>
          </el-col>
      </el-col>
       <el-col :span="11">
          <el-col :span="7" :offset="1">
              <div v-waves >
                <el-dropdown  @click="handleSearch" @command="handleMu" split-button type="primary">
                  <i class="el-icon-search" ></i>&nbsp;{{$t('table.search')}}
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="item in searchOpts" :key="item" :command="item" v-text="item" ></el-dropdown-item>
                    </el-dropdown-menu> 
                </el-dropdown>
              </div>
          </el-col>
          <el-col :span="4">
              <el-button  type="primary" @click="toClient('create')" >{{$t('table.create')}}</el-button>
          </el-col>
          <el-col  v-if="isAdmin" :span="4">
              <el-button type="primary" @click="toAssign()" >{{$t('table.assign')}}</el-button>
          </el-col>
          <el-col :span="4" style='margin-left:5px'>
              <el-button  type="danger" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button>
          </el-col>
          <el-col :span="3">
              <el-button  v-if="isAdmin" type="danger"  @click="toDelete()">{{$t('table.delete')}}</el-button>
          </el-col>
       </el-col>
      <!-- <el-button  v-show="false" class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">{{$t('table.export')}}</el-button> -->
    </el-row>
    <el-row>
        <el-form >
          <el-form-item class="filter-container" label="筛选条件:">
            <el-col :span="4">
              <el-checkbox v-model="todayFollow">仅显示今天需要跟进的</el-checkbox>
            </el-col>   
            <el-col :span="3" v-if="isAdmin">  
              <el-checkbox v-model="unAllocate">仅显示未分配的</el-checkbox>
            </el-col>   
            <el-col :span="14">  
              <el-checkbox-group v-model="statusList">
                <el-checkbox :key="index" :label="index" v-for="(val,index) in handleStatus">{{val}}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-form-item>
        </el-form>
    </el-row>
    <el-row>
      <el-table size="mini" ref="clientTable" @sort-change="sortChange"	@selection-change="handleSelectionChange" :key='tableKey' height="510" :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
        style="width: 100%">
        <el-table-column v-if="selection.show" type="selection" width="55"></el-table-column>
        <el-table-column fixed type="index" width="50"></el-table-column>
        <el-table-column fixed sortable width="110px" :sort-method="dtSort" align="center" prop="dt" :label="$t('table.dt')"></el-table-column>
        <el-table-column fixed width="80px" align="center" :label="$t('table.name')">
          <template slot-scope="scope" >
              <a href="#" @click.prevent="toClient('edit',scope.row)">{{scope.row.name}}</a>
          </template>
        </el-table-column>
        <el-table-column fixed width="150px" align="center" :label="$t('table.phone')">
          <template slot-scope="scope" @click="calling">
              <span v-html="scope.row.phone"></span>
              <!-- <el-tooltip  effect="dark" content="与客户电话沟通" placement="bottom" > -->
              <span class="icon-item"><i class="el-icon-mobile-phone"></i></span>
              <!-- </el-tooltip> -->
          </template>
        </el-table-column>
        <el-table-column  width="100px" align="center" prop="wechatName" :label="$t('table.wechatName')"></el-table-column>
        <!-- <el-table-column  width="50px"  align="center"  :label="$t('table.email')">
          <template slot-scope="scope">
            <el-tooltip  effect="light" :content="scope.row.email|empty" placement="top" >
              <div class="icon-item" @click="toMail(scope.row.emai)">
                <svg-icon icon-class="email"/>
              </div>
            </el-tooltip>
          </template>
        </el-table-column> -->
        <el-table-column  sortable width="120px" align="left" prop="address" :label="$t('table.address')"></el-table-column>
        <el-table-column  sortable width="95px" align="left" prop="amtInvest" :label="$t('table.amtInvest')"></el-table-column>
        <el-table-column  width="83px" prop="dtMeetUp" :label="$t('table.dtMeetUp')"></el-table-column>
        <el-table-column  width="83px" prop="dtSign" :label="$t('table.dtSign')"></el-table-column>
        <el-table-column  width="90px" prop="channel" :label="$t('table.channel')"></el-table-column>
        <el-table-column  sortable width="90px" prop="follower" :label="$t('table.follower')"></el-table-column>
        <el-table-column  sortable width="100px" prop="status" :label="$t('table.status')">
          <template slot-scope="scope" @click="calling">
              <span v-text="handleStatus[scope.row.status]"></span>
          </template>
        </el-table-column>
        
        <el-table-column  sortable width="83px" prop="latestTime" :label="$t('table.latestTime')"></el-table-column>
        <el-table-column  sortable width="83px" prop="nextTime" :label="$t('table.nextTime')"></el-table-column>
          
        <el-table-column  width="80px"  fixed="right" align="center" :label="$t('table.memo')">
          <template slot-scope="scope" @click="noting">
            <el-tooltip  effect="light" :content="!scope.row.memo?'点击记录沟通信息':scope.row.memo" placement="left" >
              <div class="icon-item" @click="show_memo(scope.row)">
                <svg-icon icon-class="form" />
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column fixed="right" align="center" :label="$t('table.actions')" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-col :span=24>
              <el-button type="default" size="small" @click="toFollow(scope.row)">{{$t('table.follow')}}</el-button>
            </el-col>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <div class="pagination-container">
      <el-pagination background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" :total="total">
      </el-pagination>
    </div>

    <el-dialog :title="$t('dialog.followSetting')" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="followForm" :model="client" label-position="left" >
        <el-form-item label="当前跟进进度" prop="status">
          <el-radio-group v-model="client.status">
            <el-radio v-for="(item,key,index) in handleStatus" :key="index+1" :label="index+1">{{item}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="下次跟进时间" prop="nextTime">
            <el-date-picker
              v-model="client.nextTime"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              :picker-options="nexttimeOptions">
            </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button type="primary" @click="updateData(client)">{{$t('table.save')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('table.assign')" :visible.sync="dialogAssignVisible" width="40%">
          <el-form label-position="center" >
                <el-row>
                  <el-col :offset="3">
                    <el-form-item label="跟进人">
                      <el-select v-model="followerID" placeholder="请选择">
                        <el-option
                          v-for="item in users"
                          :key="item.id" v-show="isTutors(item)"
                          :label="item.fullname"
                          :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogAssignVisible = false">{{$t('table.cancel')}}</el-button>
            <el-button type="primary" @click="handleAssign">{{$t('table.save')}}</el-button>
          </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogMemo.Visiable" fullscreen>
        <div slot="title" >
            <el-tag type="danger">{{dialogMemo.title}}</el-tag>
        </div>
        <el-form label-position="center" >
            <el-card>
                <el-row >
                  <el-col :span="4">
                      <el-form-item label-width="80px" label="沟通标签:">
                        <a href="#" @click.prevent="toLabel()"><i class="el-icon-edit"></i></a>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                       <p class="label">{{tutorTags.join(",")}}</p>
                  </el-col>
                </el-row>
            </el-card>
            <el-card class="gt">
                <el-row>
                    <el-col :span="8">
                      <el-form-item  label-width="80px" label="沟通日期:" >
                           <el-date-picker  value-format="yyyy-MM-dd" v-model="gt.dtGotong" type="date" placeholder="选择日期"> </el-date-picker>
                      </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="17">
                      <el-form-item  label-width="80px" label="沟通内容:" >
                            <el-input  v-model="gt.content"  :autosize="{ minRows: 2, maxRows: 6 }" type="textarea" ></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :offset="1" :span="6">
                      <el-form-item>
                          <el-button @click="addGt()" type="primary" icon="el-icon-plus" round>{{$t("table.add")}}</el-button>
                      </el-form-item>
                    </el-col>
                </el-row>
            </el-card>
            <el-card>
                <my-alert v-for="(g,index) of gts" :key="g.id" :title="g.title" :description="g.content" 
                   :close-text="$t('alert.delete')" @close="gtDel(g.id)" v-show="true" :type="index==0?'success':'info'">
                </my-alert>
            </el-card>
            <el-card class="upload">
                <my-upload
                  class="upload-demo"
                  action="https://api.thelittlegym.com.cn/oss/upload"
                  :on-preview="handlePreview"
                  :on-remove="handleRemove"
                  :before-upload="beforeUpload"
                  :before-remove="beforeRemove"
                  :on-success="onSuccess"
                  :limit="10"
                  :on-exceed="handleExceed"
                  :data="{FranAppId:gt.FranAppId}"
                  :file-list="fileList">
                  <el-button size="small" type="primary">上传附件</el-button>
                  <div slot="tip" class="el-upload__tip">一次上传1个文件，且不能最大超过40M</div>
                </my-upload>
            </el-card>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button style="margin-right:5%" type="danger" @click="memoExit()">{{$t('table.exit')}}</el-button>
          </span>
    </el-dialog>


    <el-dialog :title="$t('dialog.attachment')" :visible.sync="dialogFile.visible">
     <section>
       <article>
         <p v-show="isVideo(dialogFile.file.name)"><video controls="controls"><source :src="dialogFile.file.url" type="video/mp4" autobuffer=""></video></p>
         <p v-show="isImg(dialogFile.file.name)">
           <el-popover placement="right" title="" trigger="hover">
              <img :src="dialogFile.file.url"  style="max-height: 650px;max-width: 800px"/>
              <img slot="reference" :src="dialogFile.file.url" :alt="dialogFile.file.url" style="max-height: 150px;max-width: 200px">
           </el-popover>
         </p>
         <p><a @click.prevent="download(dialogFile.file.url)" download title="点击下载" target="_blank">{{dialogFile.file.name}}&nbsp;&nbsp;<svg-icon icon-class="excel" /></a></p>
       </article>
     </section>
    </el-dialog>

  <el-dialog title="添加标签" :visible.sync="dialogLabelVisiable">
     <el-card class="box-card">
        <el-tag
          :key="index"
          v-for="(tag,index) in tutorTags"
          closable
          @close="labelDel(tag)"
          :disable-transitions="false">
          {{tag}}
        </el-tag>
        <el-autocomplete
            class="input-new-tag"
            v-if="tag.inputVisible"
            v-model="tag.inputValue"
            ref="saveTagInput"
            size="small"
            :fetch-suggestions="querySearchAsync"
            @keyup.enter.native="handleInputConfirm"
            :trigger-on-focus="false"
            @select="handleSelect">
        </el-autocomplete>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
     </el-card>
    </el-dialog> 
 

    <el-dialog title="高级筛选" :visible.sync="dialogAdvVisiable" width="60%">
      <div>
        <el-row :gutter="1" :key="key" v-for="(sift,key) of sifts">
          <el-col :span="1">
             <span @click="dealSift(key)" style="cursor:pointer"> 
               <i :class="{'el-icon-plus':key==0,'el-icon-minus':key>0}"></i>
              </span>
          </el-col>
          <el-col :span="6">
            <el-select v-model="sift.item" value-key="key" placeholder="搜索项" @change="init_input(key)" clearable>
              <el-option 
                v-for="c in fieldOpt"
                :key="c.key"
                :label="c.label"
                :value="c" 
                v-show="c.show">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="sift.opr" placeholder="条件关系" width="10%">
              <el-option
                v-for="opr in oprOpt"
                :key="opr.value"
                :label="opr.label"
                :value="opr.key" v-show="sift.item&&opr.type.indexOf(sift.item.type)!=-1">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
               <template v-if="sift.item.type&&sift.item.type=='dt'">
                  <el-date-picker  width="50"
                      v-model="sift.arr"
                      :picker-options="rangeTimeOps"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      range-separator="-"
                      type="daterange"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      :default-time="['00:00:00', '23:59:59']"
                      align="left" clearable>
                  </el-date-picker>
               </template>
               <template v-else-if="sift.item.type&&sift.item.type=='follower'">
                    <el-select v-model="sift.arr" placeholder="跟进人" multiple style="width:120%">
                        <el-option
                          v-for="item in users"
                          :key="item.id" v-show="isTutors(item)"
                          :label="item.fullname"
                          :value="item.id">
                        </el-option>
                    </el-select>
               </template>               
               <template v-else-if="sift.item.type&&sift.item.type=='channel'">
                    <el-select v-model="sift.arr" placeholder="渠道" multiple style="width:120%">
                        <el-option
                          v-for="(item,index) of channels"
                          :key="index" v-show="item.isdelete==0||isSuper"
                          :label="item.name"
                          :value="item.name">
                        </el-option>
                    </el-select>
               </template>   
               <template v-else-if="sift.item.type&&sift.item.type=='label'">
                    <el-select v-model="sift.arr" placeholder="标签" multiple style="width:120%">
                        <el-option
                          v-for="(item,index) in tag&&tag.tipList"
                          :key="index"
                          :label="item.value"
                          :value="item.value">
                        </el-option>
                    </el-select>
               </template>   
               <template v-else-if="sift.item.type&&sift.item.type=='status'">
                    <el-select v-model="sift.arr" placeholder="跟进状态" multiple style="width:120%">
                        <el-option
                          v-for="(item,key,index) in handleStatus"
                          :key="index"
                          :label="item"
                          :value="key">
                        </el-option>
                    </el-select>
               </template> 
               <template v-else-if="sift.item.type&&sift.item.type=='amtInvest'">
                    <el-select v-model="sift.arr"  multiple style="width:120%">
                        <el-option
                          v-for="(item,key,index) in amtInvestOpts"
                          :key="index"
                          :label="item"
                          :value="item">
                        </el-option>
                    </el-select>
               </template> 
               <template v-else>
                    <el-input type="text" v-model="sift.value" placeholder="值"></el-input>
               </template>
          </el-col>
        </el-row>
     </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="genenateSift">{{$t('table.search')}}</el-button>
        <el-button type="default" @click="dialogAdvVisiable=false;">{{$t('table.cancel')}}</el-button>
      </span>
    </el-dialog>
 
     <el-dialog :title="dialogClient.title" :visible.sync="dialogClient.visible">
          <el-form :rules="clientRules" ref="clientForm" :model="client" label-position="center" >
                <el-row>
                  <el-col :span="10">
                      <el-form-item label-width="100px" :label="$t('table.wechatName')" prop="wechatName">
                        <el-input style='min-width:150px;' v-model="client.wechatName"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :offset="1" :span="10">
                      <el-form-item label-width="100px" :label="$t('table.followerID')"  prop="followerID">
                         <el-select v-model="client.followerID" :disabled="!isSuper"  placeholder="未分配">
                           <el-option
                             v-for="item in users"
                             :key="item.id" v-show="isTutors(item)"
                             :label="item.fullname"
                             :value="item.id">
                           </el-option>
                         </el-select>
                      </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="10">
                      <el-form-item label-width="100px"  :label="$t('table.name')" prop="name">
                        <el-input style='min-width:150px;' v-model="client.name"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :offset="1" :span="10">
                      <el-form-item  label-width="100px" :label="$t('table.dt')" prop="dt">
                        <el-date-picker type="date" placeholder="选择日期" v-model="client.dt" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>
                      </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10">
                      <el-form-item  label-width="100px" :label="$t('table.phone')" prop="phone">
                            <el-input placeholder="" style='min-width:150px;' v-model="client.phone"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :offset="1" :span="10">
                      <el-form-item label-width="100px" :label="$t('table.channel')" prop="channel">
                         <el-select v-model="client.channel" placeholder="请选择">
                           <el-option
                             v-for="item of channels"
                             :key="item.id" v-show="item.isdelete==0"
                             :label="item.name"
                             :value="item.name">
                           </el-option>
                         </el-select>
                      </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10">
                      <el-form-item  label-width="100px" :label="$t('table.address')" prop="address">
                            <el-input  style='min-width:150px;' v-model="client.address"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :offset="1" :span="10">
                      <el-form-item label-width="100px"  :label="$t('table.amtInvest')"  prop="amtInvest">
                         <el-select v-model="client.amtInvest"   placeholder="选择金额">
                           <el-option
                             v-for="(item,index) of amtInvestOpts"
                             :key="index"  
                             :label="item"
                             :value="item">
                           </el-option>
                         </el-select>
                      </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10">
                        <el-form-item label-width="100px" :label="$t('table.dtMeetUp')"  prop="dtMeetUp">
                          <el-date-picker type="datetime" placeholder="选择日期" v-model="client.dtMeetUp" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="dtMeetOptions" style="width: 100%;"></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :offset="1" :span="10">
                        <el-form-item label-width="100px" :label="$t('table.dtSign')"  prop="dtSign">
                          <el-date-picker type="datetime" placeholder="选择日期" v-model="client.dtSign" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="dtMeetOptions" style="width: 100%;"></el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                  <el-col :span="10">
                  </el-col>
                  <el-col :offset="1" :span="10">
                  </el-col>
                </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="handleSave(false)">{{$t('table.cancel')}}</el-button>
            <el-button type="primary" @click="handleSave(true)">{{$t('table.save')}}</el-button>
          </div>
      </el-dialog>
  </div>
</template>

<script>
import { fetchList, updateFollow, getChannels, deleteFranApp, updateAssign, getOss, delOss, createClient, updateClient} from '@/api/client'
import { gtList, gtSave, gtDel } from '@/api/goTong'
import { labelList, labelAdd, labelDel, labeForFranApp } from '@/api/label'
import myAlert from '@/components/ele/alert'
import myUpload from '@/components/ele/upload'
import waves from '@/directive/waves' // 水波纹指令
import { parseTime } from '@/utils'
import { mapGetters } from 'vuex';
import $ from 'jquery'

export default {
  name: 'clients',
  directives: {
    waves
  },
  components:{
    myAlert,
    myUpload
  },
  data() {
    var validate_phone = (rule, value, callback) => {
      var reg =/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
      if (!(reg).test(value)) {
        //console.log(11)
        callback(new Error('手机格式不正确！'));
      }else{
        callback();
      } 
    };
    var validate_mail = (rule, value, callback) => {
      var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); 
      if (value&&!(reg).test(value)) {
        callback(new Error('邮箱格式不正确！'));
      }else{
        callback();
      }  
    };
    return {
      amtInvestOpts: ['100-150W','150W-200W','200W以上'],
      tutorTags: [],
      statusList: [],
      tag:{inputVisible: false,inputValue: '',tipList:[]},
      fileList: [],
      dialogFile:{visible:false,file:{name:"",url:""}},
      searchOpts:["高级搜索"],
      advSearchWhere:[],
      advSearch2Where:[],
      sifts:[{item:{},opr:undefined,value:undefined,arr:[]}],
      oprOpt:[{label:"包含",key:"like '%@val%'",type:"string,object"},{label:"不包含",key:"not like '%@val%'",type:"string,object"},
                {label:"等于",key:"='@val'",type:"string,number"},{label:"不等于",key:"!='@val'",type:"string,number"},
                {label:"大于等于",key:">='@val'",type:"number"},{label:"小于等于",key:"<='@val'",type:"number"},
                {label:"大于",key:">'@val'",type:"number"},{label:"小于",key:"<'@val'",type:"number"},
                {label:"范围内",key:"between '@val1' and '@val2'",type:"dt"},{label:"范围外",key:"not between '@val1' and '@val2'",type:"dt"},
                {label:"属于",key:"in (@val)",type:"channel,follower,status,amtInvest"},{label:"不属于",key:"not in (@val)",type:"channel,follower,status,amtInvest"},
                {label:"属于",key:"in (select f.FranAppId from TLG_Labels l join TLG_LabelForFraApp f on l.name in (@val) and l.id=f.labelId)",type:"label"} 
            ],
      color:[
          "#5199EB",
          "#54B983",
          "#E7A12A",
          "#9568CE"
        ],
      tableKey: 0,
      exportList:null,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        dtzx: undefined,
        sort: undefined
      },
      nexttimeOptions:{
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
          shortcuts: [{
            text: '明天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24*1);
              picker.$emit('pick', date);
            }
          }, {
            text: '后天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24*2);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
      },
      dtMeetOptions:{
          shortcuts: [{
            text: '明天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24*1);
              picker.$emit('pick', date);
            }
          }, {
            text: '后天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24*2);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
      },
      rangeTimeOps: {
            shortcuts: [{
            text: '最近一个月',
            onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
            }
            }, {
            text: '最近三个月',
            onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
            }
            }, {
            text: '最近六个月',
            onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 180);
                picker.$emit('pick', [start, end]);
            }
            }, {
            text: '最近一年',
            onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 365);
                picker.$emit('pick', [start, end]);
            }
            }]
      },
      importanceOptions: [1, 2, 3,4],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      dialogClient:{title:'',visible:false},
      client: {
          id: undefined,
          followerID:undefined,
          channel: undefined,
          wechatName:undefined,
          email:undefined,
          phone:undefined,
          status:undefined,
          nextTime:undefined,
          dtSign:undefined,
          name:undefined,
          dt:undefined,
          wechatName:undefined,
          dtMeetUp:undefined,
          amtInvest:undefined
      },
      temp:{},
      row_cur:{},
      todayFollow:false,
      unAllocate:false,
      selection:{show:false,ids:[]},
      search:{placeholder:"搜索关键字:手机号/姓名等",value:""},
      handleStatus:{"1":"待处理","2":"处理中","3":"已建群","4":"已面谈","5":"已签约","6":"有效建群"},
      importanceOptions: [1, 2, 3,4],
      dialogFormVisible: false,
      dialogMemo: {title:"沟通记录",Visiable: false},
      dialogLabelVisiable: false,
      dialogAdvVisiable: false,
      lableVisible:false,
      dialogStatus: 'create',
      dialogPvVisible: false,
      pvData:[],
      dialogAssignVisible:false,
      users:[],
      channels:[],
      followerID:undefined,
      gt:{dtGotong:new Date(),content:"",FranAppId:undefined,userId:undefined},
      gts:[],
      rules: {
          nextTime: [{ required: false, message: '请输选择下次跟进时间', trigger: 'blur' }],
          status: [{ required: true, message: '请选择处理进度', trigger: 'blur' }],
      },
      clientRules:{
          channel: [{required: true, message: '请输入来源渠道', trigger: 'blur' }],
          phone: [{required: true, message: '请输入手机号', trigger: 'blur' }],
          name: [{required: true, message: '请输入申请人姓名', trigger: 'blur' }],
          address: [{required: true, message: '请输入申请区域', trigger: 'blur' }],
          dt: [{required: true, message: '请输入申请日期', trigger: 'blur' }]
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
    },
    empty(val){
      if(!(val&&val.trim())) return "无";
      return val;
    }
  },
  computed:{
     fieldOpt(){
        return [
                {label:"渠道",key:"channel",type:"channel",show:true},
                {label:"标签",key:"id",type:"label",show:true},
                {label:"申请区域",key:"address",type:"string",show:true},
                {label:"最后沟通时间",key:"latestTime",type:"dt",show:true},
                {label:"跟进人",key:"followerID",type:"follower",show:this.isAdmin},
                {label:"微信名",key:"wechatName",type:"string",show:true},
                {label:"投资额",key:"amtInvest",type:"amtInvest",show:true},
               // {label:"跟进状态",key:"status",type:"status",show:true}
              ]
     },
    ...mapGetters([
      'roles',
      'isAdmin',
      'isSuper',
      'userid'
    ])
  },
  methods: {
    isTutors(u){
        return u.isSuper||(u.fullname.indexOf("管理员")==-1&&u.isdelete==0);
    },
    dtSort(a,b){
      if(b.CreateTime==a.CreateTime){
			   if( b.id-a.id>0)  return -1;
			   if( b.id-a.id==0) return 0;
         return 1;
      }else if(b.CreateTime>a.CreateTime){
        return -1;
      }else{
        return 1;
      }
    },
    init_input(index){
      let sft=this.sifts[index];
      sft.arr=[];
      sft.value=undefined;
      sft.opr=undefined;
    },
    toMail(addr){
      if(!addr){
        this.$message({
            showClose: true,
            message: "没有邮箱地址,不能发邮件",
            type: 'error'
        });
      }
    },
    memoExit(){
        this.dialogMemo.Visiable = false;
    },
    addGt(){
      let self=this;
      gtSave(this.gt).then((res)=>{
        if(res.code==0){
            self.gtList()
        }
      })
    },
    sortChange(param){
        this.listQuery.sort=param.prop+" "+param.order.substring(0,param.order.indexOf("c")+1);
        if(this.listQuery.sort&&this.listQuery.sort.trim()) this.listQuery.sort+=","
        this.getList();
    },
    genenateSift:function(){
       var self=this;
       let adv2Key=['latestTime'];
       self.advSearchWhere=[];
    
       if(self.statusList.length>0){
          self.advSearchWhere.push("status in ("+self.statusList.join(",")+")");
       }
 
       self.advSearch2Where=[];
       let obj;
       self.sifts.forEach(function(s){
         if(!self.isEmpty(s.item)){
              //确认对象
              obj=self.advSearchWhere;
              if(adv2Key.indexOf(s.item.key)!=-1){
                 obj=self.advSearch2Where;
              }
              //添加
              if(s.value&&s.opr){
                  obj.push(s.item.key+" "+s.opr.replace("@val",s.value.trim()));
              }
              if(s.arr&&s.arr.length>0&&s.opr){
                  if(s.opr.indexOf("in")==-1){
                    obj.push(s.item.key+" "+s.opr.replace("@val1",s.arr[0].trim()).replace("@val2",s.arr[1].trim()));
                  }else{
                    obj.push(s.item.key+" "+s.opr.replace("@val",self.join(s.arr)));
                  }
              }

          }
       })
       self.dialogAdvVisiable=false;
       self.getList();
    },
    dealSift:function(index){
      if(index==0){
        if(this.sifts.length>this.fieldOpt.length) return;
        this.sifts.push({item:{},opr:'',value:undefined,arr:[]});
      }else{
        this.sifts.splice(index,1);
      }
    },
    handleMu:function(m){
       let self=this;
       if(m=="高级搜索"){
          this.dialogAdvVisiable=true;
          this.getLabels();
       }
    },
    gtList(){
         let self=this;
         self.gts=[];
         gtList(this.gt.FranAppId).then((res)=>{
           if(res.code==0) self.gts=res.data;
         })
    },
    gtDel(id){
        let self=this;
        this.$confirm('删除后不能恢复,是否确认删除?')
          .then(function() {
              gtDel(id).then((res)=>{
                if(res.code==0){
                    self.gtList();
                }
              })
          }).catch(action => {
              if(action === 'cancel'){
                  self.$message({
                    type: 'info',
                    message: "已取消操作"
                  })
              }
          }) 
      return false;

    },
    show_memo:function(row){
         let self=this;
         this.row_cur=row;
         this.gt.FranAppId=row.id;
         this.gt.userId=this.userid;
         this.dialogMemo.title='与"'+row.name+'"沟通记录中...'
         this.dialogMemo.Visiable=true;
         this.gtList();
         this.getOss(this.gt.FranAppId);
         self.tutorTags=[];
         labeForFranApp(this.gt.FranAppId).then((res)=>{
           if(res.code==0&&res.data){
             res.data.map(function(d){
               self.tutorTags.push(d.name);
             });
           }
         })
         
    },
    noting(){
         console.log("I'm noting")
    },
    calling(){
         console.log("I am calling!")
    },
    getList() {
      this.listLoading = true
      this.list =[];
      fetchList(this).then(response => {
        //前而request.js做了处理
        if(response.code==0){
           let res=response.data;
           if(res.length>0){
             this.list = res;
             this.total = res&&res[0].total;
             //console.log(this.list)
           }
        }
        this.listLoading = false
      })
    },
    handleSearch(){
      this.listQuery.page = 1
      this.genenateSift()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.genenateSift()
    },
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.genenateSift()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.genenateSift()
    },
    resetTemp() {
      this.client={
          id: undefined,
          followerID:undefined,
          channel: undefined,
          wechatName:undefined,
          email:undefined,
          phone:undefined,
          status:undefined,
          nextTime:undefined,
          dtSign:undefined,
          name:undefined,
          dtMeetUp:undefined,
          dt:undefined,
          amtInvest:undefined
      }
    },
    toClient(type,row) {
      if(type=='create'){
         this.resetTemp();
         Object.assign(this.temp,this.client); //初始状态保存
         this.client.followerID=this.userid;
         this.dialogClient.title = '新建';
         this.handleSave=this.handleCreate;
      }else{
         this.temp = Object.assign({},row) //原数据备份
         this.client=row;
         if(row.followerID==0)this.client.followerID=undefined;
         this.dialogClient.title = '编辑';
         this.handleSave=this.handleUpdate;
      }
      this.dialogClient.visible = true
      this.$nextTick(() => {
        this.$refs['clientForm'].clearValidate()
      })
    },
    handleCreate(noCancel) {
      let self=this;
      if(!noCancel){
         self.dialogClient.visible = false;
      }
      this.$refs['clientForm'].validate((valid) => {
        if (valid) {
          createClient({
              UserName:this.client.name,UserPhone:this.client.phone,
              UserEmail:this.client.email,dtSign:this.client.dtSign,
              Channel:this.client.channel,City:this.client.address,
              dt:this.client.dt,followerId:this.client.followerID,
              wechatName:this.client.wechatName,Remark:"后台,建入",
              amtInvest:this.client.amtInvest
          }).then((res) => {
              if(res.code==0){
                  self.dialogClient.visible = false
                  self.$notify({
                    title: '创建成功',
                    type: 'success',
                    duration: 2000
                  })
              } 
              self.search.value="";
              self.advSearchWhere=[];
              self.advSearch2Where=[];
              self.getList();
              Object.assign(self.client,self.this.temp);//清空
               
          })
        }
      })
    },
    handleUpdate(noCancel) {
      let self=this;
      if(!noCancel){
         Object.assign(this.client,this.temp);
         self.dialogClient.visible = false;
         return;
      }
      this.$refs['clientForm'].validate((valid) => {
        if (valid) {
          updateClient(this.client).then((res) => {
            if(res.code==0){
                self.dialogClient.visible = false
                self.$notify({
                  title: '更新成功',
                  type: 'success',
                  duration: 2000
                })
            }else{
                self.client=self.temp;
            }
          })
        }
      })
    },
    toFollow(row){
         this.row_cur=row;
         this.client.id=row.id;
         this.client.status=row.status;
         this.client.nextTime=row.nextTime;
         this.dialogFormVisible=true;
    },
    updateData(data) {
      updateFollow(data).then((res)=>{
        if(res.code==0){
          this.row_cur.status=data.status.valueOf();
          this.row_cur.nextTime=data.nextTime;
          this.$message({
              showClose: true,
              message: res.msg,
              type: 'success'
          });
          this.dialogFormVisible=false;
        }
      })
    },
    toAssign(){
        let self=this;
        if(!this.selection.ids.length||!this.selection.show){
          self.$message({
            type: 'info',
            message: '请先勾选需分配的记录,再点击按钮',
            duration: 4 * 1000
          });     
          this.selection.show=true;
          this.selection.ids=[];
          return;
        }
        self.dialogAssignVisible=true;
    },
    handleAssign(){
        let self=this;
        if(!this.followerID){
            self.$notify({
                title: '错误',
                message: "请选择要分配的老师",
                type: 'error',
                duration: 2000
            })
            return;
        }
        updateAssign(this).then(function(res){
          if(res.code==0){
              self.$notify({
                title: '分配成功',
                type: 'success',
                duration: 2000
              })
              self.selection.ids=[];
              self.selection.show=false;
              self.dialogAssignVisible=false;
              self.getList();
          }else{
              self.$notify({
                title: '错误',
                message: res.msg,
                type: 'error',
                duration: 2000
              })
          }
        })
    },
    toDelete(){
        let self=this;
        if(!this.selection.ids.length||!this.selection.show){
          self.$message({
            type: 'info',
            message: '请先选择需要删除的记录'
          });     
          this.selection.show=true;
          this.selection.ids=[];
          return;
        }
        this.$confirm('是否确认删除?')
          .then(function() {
              self.handleDelete();
          }).catch(action => {
              if(action === 'cancel'){
                  self.$message({
                    type: 'info',
                    message: "已取消操作"
                  })
              }
              self.selection.show=false;
              self.selection.ids=[];
              this.$refs.clientTable.clearSelection();
          }) 
    },
    handleDelete() {
      let self=this;
      deleteFranApp(this).then(function(res){
         if(res.code==0){
            self.$notify({
              title: '删除成功',
              type: 'success',
              duration: 2000
            })
            let indexes=self.selection.ids.map(function(id){
                  return self.list.findIndex(function(l){
                     return  l.id==id;
                  })
            })
            //console.error(indexes)
            indexes.forEach(function(i){
                self.list.splice(i,1);
            })
            self.selection.ids=[];
            self.selection.show=false;
         }else{
            self.$notify({
              title: '错误',
              message: res.msg+',删除失败',
              type: 'error',
              duration: 2000
            })
         }
      })
    },
    handleSelectionChange(vals) {
      var self=this;
      self.selection.ids=[];
      vals.forEach(function(v){
          self.selection.ids.push(v.id);
      })
    },
    handleDownload() {
      let self=this;
      this.downloadLoading = true
      fetchList(this,10000).then(response => {
        if(response.code==0&&response.data&&response.data.length>0){
          this.exportList = response.data.map((d)=>{
               d['跟进状态']=self.handleStatus[d.status]; //增加一个处理后的字段
               return d;
          });
          import('@/vendor/Export2Excel').then(excel => {
            const tHeader = ['申请人', '手机', '邮箱', '申请区域', '申请日期','投资金额','面议日期','签约日期','来源渠道','最近沟通时间','最近沟通内容','跟进人','跟进状态']
            const filterVal = ['name', 'phone','email','address', 'dt','amtInvest','dtMeetUp','dtSign','channel','latestTime','memo','follower','跟进状态']
            const data = this.formatJson(filterVal, this.exportList);
            const time=this.fmtDt("yyyy-MM-dd-hh:mm:ss")(new Date);
            excel.export_json_to_excel({
              header: tHeader,
              data,
              filename: '加盟申请清单_'+time
            })
            this.downloadLoading = false
          })
        }
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
     },
     submitForm(formName) {
        var self=this;
        self.$refs[formName].validate((valid) => {
          if (valid) {
              self.saveUser(formName);
          } else { 
              this.$message({
                type: 'error',
                message: `内容输入不正确！`
              });
              return false;
          }
        });
      },
      saveUser(formName){

      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.temp.Tags=[];
        this.$refs.username.$el.querySelector('input').focus();
        this.temp.gym_selected=JSON.stringify(this.account.gyms[0]);
        this.temp.ls_selected=this.account.id;
      },
      handleRemove(file, fileList) {
         let name=file.url.split("/");
         name=name[name.length-1];
         let FranAppId=this.gt.FranAppId
         delOss({name,FranAppId})
      },
      getOss(FranAppId){
          let self=this;
          self.fileList=[];
          getOss(FranAppId).then((res)=>{
             if(res.code=200&&res.data){
                res.data.map((d)=>{
                   let url="http://static.thelittlegym.com.cn/"+d.name;
                   let name=d.name.split("/");
                   self.fileList.push({name:name[name.length-1],url:url})
                })
             }
             //console.log(self.fileList);
          });
      },
      onSuccess(res,file,fileList){
        if(res.code==200){
          fileList[fileList.length - 1].url=res['oss-request-url'];
        }
      },
      handlePreview(file) {
         let self=this;
         this.dialogFile.visible=true;
         this.$nextTick(function(){
           self.dialogFile.file=file;
         })
      },
      handleExceed(files, fileList) {
        //console.log(JSON.stringify(files))
        this.$message.warning(`当前限制选择 8 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      beforeUpload(file){
          let _this = this;
          const isgt40M = file.size / 1024 / 1024 < 40;
          if (!isgt40M) {
            _this.$message.error('文件大小不能超过40MB!');
          }
          if (isgt40M){
            return true;
          } else {
            return false;
          }
      },
      isVideo(file){
          let ext=file.split(".")[1];
          let support_form=['wav', 'mp3', 'ogg', 'acc', 'webm'];
          if(file&&support_form.indexOf(ext.toLowerCase())!=-1){
            return true;
          }
          return false;
      },
      isImg(file){
          let ext=file.split(".")[1];
          let support_form=['gif', 'png', 'bmp' ,'jpg'];
          if(file&&support_form.indexOf(ext.toLowerCase())!=-1){
            return true;
          }
          return false;
      },
      showInput() {
          this.tag.inputVisible = true;
          this.$nextTick(_ => {
            this.$refs.saveTagInput.$refs.input.focus();
          });
      },
      toLabel(){
          this.dialogLabelVisiable=true;
          this.getLabels();
      },
      getLabels(){
          let self=this;
          labelList().then((res)=>{
              if(res.code==0)self.tag.tipList=res.data;
          })
      },
      handleInputConfirm() {
         this.labelAdd(this.tag.inputValue)
      },
      querySearchAsync(queryString, cb) {
          let programList = this.tag.tipList;
          let results = queryString ? programList.filter((item)=>{
              return item.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1;
          }): programList;
          clearTimeout(this.timeout);
          this.timeout = setTimeout(() => {
            cb(results);
          }, 1000 * Math.random());
            
      },
      handleSelect(item) {
          this.tag.inputValue = item.value;
          this.labelAdd(this.tag.inputValue)
      },
      labelAdd(name){
          let self=this;
          if(this.tutorTags.indexOf(name)!=-1){
              this.$message({
                  showClose: true,
                  message: "标签已存在",
                  type: 'error'
              });
              this.tag.inputVisible = false;
              this.tag.inputValue = '';
              return;
          }
          labelAdd({name,FranAppId:this.gt.FranAppId}).then((res)=>{
              if(res.code==0){
                  self.tutorTags.push(name);
              }
          });
          this.tag.inputVisible = false;
          this.tag.inputValue = '';
      },
      labelDel(name){
        let self=this;
        labelDel({name,FranAppId:this.gt.FranAppId}).then((res)=>{
          if(res.code==0){
              let i =self.tutorTags.indexOf(name);
              self.tutorTags.splice(i,1);
          }
        })
      }

  },
  watch:{
     todayFollow(){
       this.handleSearch();
     },
     unAllocate(){
       this.handleSearch();
     },
     statusList(){
       this.handleSearch();
     }
  },
  mounted(){
    let self=this;
    this.getList();
    this.getUsers();
    getChannels().then((res)=>{
      if(res.code==0&&res.data){ 
          self.channels=res.data;
      }
    })
  }
}
</script>
<style  scoped>
    .icon-item {
      font-size: 1.5em;
      color: #24292e;
      cursor: pointer;
    }
    .w-50{
      width: 46%;
      margin-right:3%; 
      display: inline-block;
    }
    .search-input {
      width: 280px;
      box-sizing: border-box;
      font-family: 'MicrosoftYaHei';
      font-size: 14px;
      color: #909399;
      border-radius: 0;
    }
 
 .color{
    color:#409eff;
    background-color: #ecf5ff;
    border-color: #c6e2ff;
  }
  .nocolor{
    background: #fff;
    border: 1px solid #dcdfe6;
    color: #606266;
  }
  .box-card .w-50{
      width: 48%;
      display: inline-block;
  }
  .card-right{
    height:100%;
    box-shadow:none;
    border-bottom: none;
    border-right: none;
    position: relative;
  }
  .btnn{
      margin: 10px 5px 5px 10px !important;
    }
  
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
  .label{
    width:400px;height:50px; word-wrap: break-word;
  }
  .gt{
     margin-top:2%
  }
  .cell>a{
     text-decoration:underline !important;
  }
  article>p>a{
    text-decoration:underline !important;
    color:#C00017 !important; 
  }
  article>p{
    text-align:center;
  }

  .el-tag + .el-tag {
    margin-right: 10px;
    float:left;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 130px;
    margin-left: 10px;
    vertical-align: bottom;
  }
  .filter-container{
    margin-top: 12px;
    margin-bottom: 0;
  }
</style>
