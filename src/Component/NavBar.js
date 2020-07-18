import React, { Component } from 'react';
import {Navbar, Nav, Container} from 'react-bootstrap';
import { Link, NavLink } from "react-router-dom";
import ReactTooltip from "react-tooltip";

import Switcher from "./Switcher";
import Searcher from "./Searcher";
import Bookmark from "./Bookmark";

import "./Components.css";


class NavBar extends Component {
    constructor(props) {
        super(props);
    }

    // renderSwitcher() {
    //     if(!this.props.isBookmarkClicked && !this.props.isOnSearch)
    //     {
    //         return (
    //             <Container>
    //
    //                 <Navbar.Text className="text-light p-2">NYTimes</Navbar.Text>
    //                 <Switcher
    //                     className="mt-1"
    //                     switchApiBase={this.props.switchApiBase}
    //                     apiBase={this.props.apiBase}
    //                 />
    //                 <Navbar.Text className="text-light p-2">Guardian</Navbar.Text>
    //             </Container>
    //         )
    //     }
    // }

    render() {
        if(!this.props.isBookmarkClicked && !this.props.isOnSearch && !this.props.isOnArticle)
            return (
                <Navbar bg="dark" expand="lg" variant="dark" className="nav-bar">
                    <Navbar.Brand className="mr-0">
                        <div className="search-bar">
                            <Searcher isOnSearch={this.props.isOnSearch} isOnArticle={this.props.isOnArticle}/>
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto">
                            <Nav.Link as={NavLink} exact to="/">Home</Nav.Link>
                            <Nav.Link as={NavLink} to="/world">World</Nav.Link>
                            <Nav.Link as={NavLink} to="/politics">Politics</Nav.Link>
                            <Nav.Link as={NavLink} to="/business">Business</Nav.Link>
                            <Nav.Link as={NavLink} to="/technology">Technology</Nav.Link>
                            <Nav.Link as={NavLink} to="/sports">Sports</Nav.Link>
                        </Nav>
                        <Nav className="mr-sm-2">
                            <Nav.Link as={Link} to="/bookmark" className="text-light" data-tip="Bookmark">
                                <Bookmark
                                    isBookmarkClicked={this.props.isBookmarkClicked}
                                    clickBookmark={this.props.clickBookmark}
                                />
                            </Nav.Link>
                            <ReactTooltip />

                            <Navbar.Text className="text-light pr-2">NYTimes</Navbar.Text>
                            <div className="switcher">
                                <Switcher
                                    switchApiBase={this.props.switchApiBase}
                                    apiBase={this.props.apiBase}
                                />
                            </div>
                            <Navbar.Text className="text-light">Guardian</Navbar.Text>

                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            )
        else {
            return (
                <Navbar bg="dark" expand="lg" variant="dark" className="nav-bar">
                    <Navbar.Brand className="mr-0">
                        <div className="search-bar">
                            <Searcher isOnSearch={this.props.isOnSearch} isOnArticle={this.props.isOnArticle} />
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto">
                            <Nav.Link as={NavLink} exact to="/">Home</Nav.Link>
                            <Nav.Link as={NavLink} to="/world">World</Nav.Link>
                            <Nav.Link as={NavLink} to="/politics">Politics</Nav.Link>
                            <Nav.Link as={NavLink} to="/business">Business</Nav.Link>
                            <Nav.Link as={NavLink} to="/technology">Technology</Nav.Link>
                            <Nav.Link as={NavLink} to="/sports">Sports</Nav.Link>
                        </Nav>
                        <Nav className="mr-sm-2">
                            <Nav.Link as={Link} to="/bookmark" className="text-light" data-tip="Bookmark">
                                <Bookmark
                                    isBookmarkClicked={this.props.isBookmarkClicked}
                                    clickBookmark={this.props.clickBookmark}
                                />
                            </Nav.Link>
                            <ReactTooltip />

                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            )
        }
    }
}

export default NavBar;

