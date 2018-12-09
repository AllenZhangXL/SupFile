import React, { Component } from "react";
import "./css/main.css";
import "./css/files.css";

import fileImg from "./images/file.svg";
import folderImg from "./images/folder.svg";
import uploadImg from "./images/upload.png";
import downloadImg from "./images/download.svg";
import newFolderImg from './images/newfolder.png';
import backImg from './images/back.png';
import renameImg from './images/rename.svg'

import logo from "./images/logo.png";

class Files extends Component {
  constructor(props) {
    super(props);
    this.state = {
      files: null,
      currentFolder: 1,
      previousFolder: null,
      filesOption: false,
      uploadForm: false,
      fileInput: null,
      folderNameInput: null,
      newFolder: false,
      rename: false,
      itemNameInput: null,
      originalItemId: null
    };

    this.itemClicked = this.itemClicked.bind(this);
    this.logoutClicked = this.logoutClicked.bind(this);
    this.requestFolder = this.requestFolder.bind(this);
    this.filesOptionClicked = this.filesOptionClicked.bind(this);
    this.backClicked = this.backClicked.bind(this);
    this.actionClicked = this.actionClicked.bind(this);
    this.cancelAction = this.cancelAction.bind(this);
    this.fileOnChange = this.fileOnChange.bind(this);
    this.submitFile = this.submitFile.bind(this);
    this.downloadItem = this.downloadItem.bind(this);
    this.folderNameOnChange = this.folderNameOnChange.bind(this);
    this.createFolder = this.createFolder.bind(this);
    this.itemNameOnChange = this.itemNameOnChange.bind(this);
    this.renameItem = this.renameItem.bind(this);
  }

  renameClicked(itemId) {
    this.setState({rename: true, originalItemId: itemId})
  }

  renameItem(e) {
    e.preventDefault()
    fetch("http://api.supfile.io/WebApi/rest/file/rename", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        newFileName: this.state.itemNameInput,
        superId: this.state.currentFolder,
        ownerId: this.props.user.id,
        itemId: this.state.originalItemId
      })
    }).then(this.setState({itemNameInput: null, rename: false, originalItemId: null}))
      .then(res => res.json())
      .then(data => {
        if (data.status == 200) {
          this.setState({ files: data.items})
        }
      })
  }

  itemNameOnChange(e) {
    this.setState({itemNameInput: e.target.value})
  }

  folderNameOnChange(e) {
    this.setState({folderNameInput: e.target.value})
  }

  createFolder(e) {
    e.preventDefault()
    fetch("http://api.supfile.io/WebApi/rest/file/folder", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        folderName: this.state.folderNameInput,
        superId: this.state.currentFolder,
        ownerId: this.props.user.id
      })
    }).then(this.setState({folderNameInput: null, newFolder: false}))
      .then(res => res.json())
      .then(data => {
        if (data.status == 200) {
          this.setState({ files: data.items})
        }
      })
  }

  downloadItem(itemId) {
    fetch("http://api.supfile.io/WebApi/rest/file/download/" + itemId), {method: "GET"}
  }

  fileOnChange(e) {
    this.setState({fileInput: e.target.files[0]})
  }

  submitFile(e) {
    e.preventDefault()
    let formData = new FormData();
    formData.append('file',this.state.fileInput)
    formData.append('id', this.props.user.id)
    formData.append('superId', this.state.currentFolder)
    formData.append('isFolder', false)
    fetch("http://api.supfile.io/WebApi/rest/file/upload", {
      method: "POST",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      body: formData
    })
      .then(this.setState({fileInput: null, uploadForm: false}))
      .then(res => res.json())
      .then(data => {
        if (data.status == 200) {
          this.setState({ files: data.items})
        }
      })
  }

  actionClicked(action) {
    if (action == 'upload') {
      this.setState({uploadForm: true})
    } else if (action == 'newFolder') {
      this.setState({newFolder: true})
    }
  }

  cancelAction(action) {
    if (action == 'upload') {
      this.setState({uploadForm: false, fileInput: null})
    } else if (action == 'newFolder') {
      this.setState({newFolder: false, folderNameInput: null})
    } else if (action == 'rename') {
      this.setState({rename: false, originalItemId: null, itemNameInput: null})
    }
  }

  backClicked() {
    this.requestFolder(this.state.previousFolder)
  }

  requestFolder(folderId) {
    if (folderId == 1) {
      this.setState({previousFolder: null})
    }
    fetch("http://api.supfile.io/WebApi/rest/file/get_items", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        superId: folderId,
        ownerId: this.props.user.id
      })
    })
      .then(res => res.json())
      .then(files => this.setState({ files: files}))
  }

  filesOptionClicked() {
    this.requestFolder(1)
    this.setState({filesOption: true, previousFolder: null})
  }

  logoutClicked() {
    this.props.logout();
  }

  itemClicked(itemId, isFolder) {
    if (isFolder) {
      this.requestFolder(itemId);
      if (itemId != 1) {
        this.setState({previousFolder: this.state.currentFolder})
      }
    } else {
      this.downloadItem(itemId)
    }
  }

  componentWillMount() {
    this.requestFolder(1)
  }

  render() {
    return (
      <div id="container">
        <div id="header">
          <div id="logo">
            <a href="/">
              <img src={logo} />
            </a>
          </div>
          <div id="available">
            <p>Available Space: {this.props.user.availableSpace} GB</p>
            <progress value={30.0 - this.props.user.availableSpace} max="30" />
          </div>
        </div>

        <div id="mainContent">
          <div id="leftBar">
            <div id="filesOption" onClick={this.filesOptionClicked} className={this.state.filesOption ? "optionSelected" : undefined}>
              <p>Files</p>
            </div>
            <div id="settingsOption">
              <p>Settings</p>
            </div>
            <div onClick={this.logoutClicked} id="logout">
              <p>Log out</p>
            </div>
          </div>

          <div id="files">
            <div id="operationBar" >
              {this.state.previousFolder &&
                <div onClick={this.backClicked} id="backAction">
                <img src={backImg}/>
                  <p>Back</p>
                </div>
              }
              
              <div onClick={() => this.actionClicked('upload')} id="uploadAction">
                <img src={uploadImg}/>
                <p>Upload</p>
              </div>
              <div onClick={() => this.actionClicked('newFolder')} id="newFolderAction">
                <img src={newFolderImg}/>
                <p>New Folder</p>
              </div>
            </div>

            <div id="fileList">
              <ul>
                {this.state.files.files.map(file => (
                  <li key={file.id} className="fileItem">
                    <img src={file.isFolder ? folderImg : fileImg} />
                    <p onClick={() => {this.itemClicked(file.id, file.isFolder)}} >
                      {file.name}
                    </p>
                    <img onClick={() => this.renameClicked(file.id)} className="renameImg" src={renameImg} />
                    {!file.isFolder && (
                      <img onClick={() => this.downloadItem(file.id)} className="downloadImg" src={downloadImg} />
                    )}
                    
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>

        {this.state.uploadForm && 
          <div id="uploadForm">
            <form onSubmit={this.submitFile}>
              <input type="file" onChange={this.fileOnChange} />
              <button className="cancelbtn" onClick={() => this.cancelAction('upload')} type="button">Cancel</button>
              <button type="submit">Upload</button>
            </form>
          </div>
        }

        {this.state.newFolder && 
          <div id="newFolderForm">
            <form onSubmit={this.createFolder}>
              <input type="text" required onChange={this.folderNameOnChange} />
              <button className="cancelbtn" onClick={() => this.cancelAction('newFolder')} type="button">Cancel</button>
              <button type="submit">Create</button>
            </form>
          </div>
        }

        {this.state.rename && 
          <div id="renameForm">
            <form onSubmit={this.renameItem}>
              <input type="text" required onChange={this.itemNameOnChange} />
              <button className="cancelbtn" onClick={() => this.cancelAction('rename')} type="button">Cancel</button>
              <button type="submit">Rename</button>
            </form>
          </div>
        }
      </div>
    );
  }
}

export default Files;
