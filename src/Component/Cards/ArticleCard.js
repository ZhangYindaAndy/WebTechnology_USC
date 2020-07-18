import React, { Component } from "react";
import {Card, Row, Col, Container, Image} from "react-bootstrap";
import commentBox from "commentbox.io";

import { FacebookShareButton, TwitterShareButton, EmailShareButton } from "react-share";
import { FacebookIcon, TwitterIcon, EmailIcon } from "react-share";
import { FaRegBookmark, FaBookmark } from "react-icons/fa";
import { IoIosArrowDown, IoIosArrowUp } from "react-icons/io";
import {toast, Zoom} from "react-toastify";

import "react-toastify/dist/ReactToastify.css";
import "../Components.css";
import ReactTooltip from "react-tooltip";


export default class ArticleCard extends Component {
    constructor(props) {
        super(props);

        let savedInfo = localStorage.getItem("savedInfo");
        let bookState;
        if(savedInfo == null)
            bookState = false;
        else {
            savedInfo = JSON.parse(savedInfo);
            if(this.props.pageData["article-id"] in savedInfo)
                bookState = true;
            else
                bookState = false;
        }

        // let isArrowNeeded = true;
        let TempColapsedText = "";
        let sentences = this.props.pageData["description"].match( /[^\.!\?]+[\.!\?]+/g );
        // console.log(sentences);
        // if(sentences.length <= 4)
        //     isArrowNeeded = false;
        if(sentences.length > 4)
        {
            for(let i = 0; i < 4; ++i)
                TempColapsedText += sentences[i];
        }

        this.state = {
            isBookSaved: bookState,
            isScrolled: false,
            ColapsedText: TempColapsedText
        }
        this.myref = React.createRef();
    }

    componentDidMount() {
        this.removeCommentBox = commentBox("5704448038928384-proj", {
            defaultBoxId: this.props.pageData["article-id"]
        });
    }

    componentWillUnmount() {
        this.removeCommentBox();
    }

    handleBookSave() {
        this.setState({
            isBookSaved: !this.state.isBookSaved
        })
        // the change of state is lazy
        if(!this.state.isBookSaved) {
            toast("Saving " + this.props.pageData["title"], {
                className: "toast-massage",
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: true,
                closeOnClick: true,
                pauseOnHover: false,
                draggable: true,
                transition: Zoom
            });
            // insert it into localStorage
            let savedInfo = localStorage.getItem("savedInfo");
            if(savedInfo == null)
                savedInfo = {};
            else
                savedInfo = JSON.parse(savedInfo);
            savedInfo[this.props.pageData["article-id"]] = this.props.pageData;
            localStorage.setItem("savedInfo", JSON.stringify(savedInfo));

        }
        else {
            toast("Removing - " + this.props.pageData["title"], {
                className: "toast-massage",
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: true,
                closeOnClick: true,
                pauseOnHover: false,
                draggable: true,
                transition: Zoom
            });
            // delete it from localStorage
            let savedInfo = localStorage.getItem("savedInfo");
            savedInfo = JSON.parse(savedInfo);
            delete savedInfo[this.props.pageData["article-id"]];
            localStorage.setItem("savedInfo", JSON.stringify(savedInfo));
        }
    }

    renderBookmark() {
        if(!this.state.isBookSaved)
            return (
                <FaRegBookmark
                    style={{color: "red"}}
                    onClick={this.handleBookSave.bind(this)}
                    data-tip="Bookmark"
                />
            )
        else
            return (
                <FaBookmark
                    style={{color: "red"}}
                    onClick={this.handleBookSave.bind(this)}
                    data-tip="Bookmark"
                />
            )
    }

    scrollDown() {
        this.setState({ isScrolled: true });
        this.myref.current.scrollIntoView({ behavior: "smooth" });
    }

    scrollUp() {
        this.setState({ isScrolled: false });
        this.myref.current.scrollIntoView({ behavior: "smooth" });
    }

    renderScorll() {
        if(this.state.ColapsedText === "")
            return null;

        if(!this.state.isScrolled)
        {
            return (
                <IoIosArrowDown
                    onClick={this.scrollDown.bind(this)}
                />
            )
        }
        else
        {
            return (
                <IoIosArrowUp
                    onClick={this.scrollUp.bind(this)}
                />
            )
        }
    }

    // componentDidUpdate(prevProps, prevState, snapshot) {
    //     console.log(prevState.state.isScrolled);
    //     console.log(this.state.isScrolled);
    //     if(this.state.isScrolled != prevState.state.isScrolled)
    //         this.myref.current.scrollIntoView({ behavior: "smooth" });
    // }

    renderText() {
        if(this.state.ColapsedText === "")
            return this.props.pageData["description"];

        if(!this.state.isScrolled)
        {
            return (
                <div>
                    {this.state.ColapsedText}
                    {/*<div ref={this.myref}></div>*/}
                </div>
            )
        }
        else
        {
            return (
                <div>
                    {this.props.pageData["description"]}
                    {/*<div ref={this.myref}></div>*/}
                </div>

            )
        }

    }

    render() {
        // console.log(this.props.pageData["image"]);
        return (
            <Container fluid>
                <Card className="border rounded bg-white m-2 shadow" style={{cursor: "pointer"}}>
                    <Card.Body className="m-3">
                    <Row>
                        <Col>
                            <Card.Title className="font-italic">
                                {this.props.pageData["title"]}
                            </Card.Title>
                        </Col>
                    </Row>

                    <Row lg={3} sm={3} xs={3}>
                        <Col lg={8} sm={6} xs={5} className="pr-0 font-italic">
                            {this.props.pageData["date"]}
                        </Col>

                        <Col lg={3} sm={5} xs={5} className="text-right pl-0">
                            <FacebookShareButton
                                url={this.props.pageData["section-url"]}
                                hashtag={"#CSCI_571_NewsApp"}
                                data-tip="Facebook"
                            >
                                <FacebookIcon round={true} size={20} />
                            </FacebookShareButton>

                            <TwitterShareButton
                                url={this.props.pageData["section-url"]}
                                hashtags={["CSCI_571_NewsApp"]}
                                data-tip="Twitter"
                            >
                                <TwitterIcon round={true} size={20} />
                            </TwitterShareButton>

                            <EmailShareButton
                                url={this.props.pageData["section-url"]}
                                subject={"#CSCI_571_NewsApp"}
                                data-tip="Email"
                            >
                                <EmailIcon round={true} size={20} />
                            </EmailShareButton>

                            <ReactTooltip />
                        </Col>

                        <Col lg={1} sm={1} xs={2} className="text-right">
                            {this.renderBookmark()}
                        </Col>
                    </Row>

                    <Row>
                        <Col lg={12} sm={12}>
                            <Image
                                className="mt-3"
                                fluid
                                src={this.props.pageData["image"]}
                            />
                        </Col>
                    </Row>

                    <Row>
                        <Col>
                            <Card.Text className="text-justify">
                                {this.renderText()}
                            </Card.Text>
                        </Col>
                    </Row>

                    <Row>
                        <Col className="text-right">
                            {this.renderScorll()}
                        </Col>
                    </Row>
                    </Card.Body>
                </Card>

                <div ref={this.myref} className="commentbox" />
            </Container>
        )

    }
}

