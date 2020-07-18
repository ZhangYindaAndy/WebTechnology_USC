import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import NavBar from './Component/NavBar';
import {ToastContainer} from "react-toastify";

import BookmarkPage from "./Component/Pages/BookmarkPage";
import SearchPage from "./Component/Pages/SearchPage";
import ArticlePage from "./Component/Pages/ArticlePage";
import PageBody from "./Component/PageBody";

import "react-toastify/dist/ReactToastify.css";
import "./Component/Components.css";



// guardian -> true, nytimes -> false
class App extends Component {
  constructor() {
      super();
      let historyApiBase = localStorage.getItem("apiBase");
      // console.log(historyApiBase);
      if(historyApiBase == null)
          historyApiBase = false;
      else
      {
          historyApiBase = JSON.parse(historyApiBase);
          historyApiBase = historyApiBase.state;
          // console.log(historyApiBase);
      }

      this.state = { apiBase: historyApiBase, isBookmarkClicked: false, isOnSearch: false, isOnArticle: false };
  }

  switchApiBase()
  {
      let tmpState = this.state.apiBase;  // use a tmp, setState is async func
      this.setState({
          apiBase: !this.state.apiBase
      })
      localStorage.setItem("apiBase", JSON.stringify({"state": !tmpState}));
      // console.log(localStorage.getItem("apiBase"));
  }

    clickBookmark() {
        this.setState({
            isBookmarkClicked: true
        });
    }

    resetBookmark() {
      this.setState({
          isBookmarkClicked: false
      })
    }

    handleOnSearch() {
      this.setState({
          isOnSearch: true
      })
    }

    resetOnSearch() {
      this.setState({
          isOnSearch: false
      })
    }

    handleOnArticle() {
      this.setState({
          isOnArticle: true
      })
    }

    resetOnArticle() {
      this.setState({
          isOnArticle: false
      })
    }


  render() {
    return (
        <BrowserRouter>
            <ToastContainer
                toastClassName="toast-massage"
                position="top-center"
                autoClose={2000}
                hideProgressBar
                newestOnTop
                closeOnClick={true}
                rtl={false}
                pauseOnVisibilityChange={false}
                draggable
                pauseOnHover={false}
            />

            <NavBar
                switchApiBase={this.switchApiBase.bind(this)}
                apiBase={this.state.apiBase}
                clickBookmark={this.clickBookmark.bind(this)}
                isBookmarkClicked={this.state.isBookmarkClicked}
                isOnSearch={this.state.isOnSearch}
                isOnArticle={this.state.isOnArticle}
            />
            <Switch>
                {/* main page */}
                <Route path="/" exact component={ () => {
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <PageBody apiBase={this.state.apiBase} section={"/"}/>
                } } />

                <Route path="/world" exact component={ () => {
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <PageBody apiBase={this.state.apiBase} section={"/world"}/>
                } } />

                <Route path="/politics" exact component={ () => {
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <PageBody apiBase={this.state.apiBase} section={"/politics"}/>
                } } />

                <Route path="/business" exact component={ () => {
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <PageBody apiBase={this.state.apiBase} section={"/business"}/>
                } } />

                <Route path="/technology" exact component={ () => {
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <PageBody apiBase={this.state.apiBase} section={"/technology"}/>
                } } />

                <Route path="/sports" exact component={ () => {
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <PageBody apiBase={this.state.apiBase} section={"/sport"}/>
                } } />

                {/* other page */}

                <Route path="/bookmark" component={() => {
                    if(!this.state.isBookmarkClicked)
                        this.clickBookmark();
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <BookmarkPage/>
                } } />

                <Route path="/search/:q" component={ (props) => {
                    if(!this.state.isOnSearch)
                        this.handleOnSearch();
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(this.state.isOnArticle)
                        this.resetOnArticle();
                    return <SearchPage {...props} apiBase={this.state.apiBase} />
                } } />

                <Route path="/article" component={(props) => {
                    if(this.state.isOnSearch)
                        this.resetOnSearch();
                    if(this.state.isBookmarkClicked)
                        this.resetBookmark();
                    if(!this.state.isOnArticle)
                        this.handleOnArticle();
                    return <ArticlePage {...props} />
                } } />
            </Switch>
        </BrowserRouter>
    );
  }
}

export default App;
