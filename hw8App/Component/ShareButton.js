import React, { Component } from "react";
import { IoMdShare } from "react-icons/io";
import {Col, Container, Modal, Row} from "react-bootstrap";
import { FacebookShareButton, TwitterShareButton, EmailShareButton } from "react-share";
import { FacebookIcon, TwitterIcon, EmailIcon } from "react-share";

import "./Components.css"


export default class ShareButton extends Component {
    constructor(props) {
        super(props);
        this.state = { showModal: false };
    }

    handleShow = (event) => {
        event.stopPropagation();
        this.setState({ showModal: true});
    }
    handleHide = () => {
        this.setState({showModal: false});
    }

    render () {
        return (
            <div className="inline-display">
                <IoMdShare onClick={this.handleShow.bind(this)} />
                <div onClick={ (event) => { event.stopPropagation(); } }>
                    <Modal show={this.state.showModal} onHide={this.handleHide.bind(this)}>
                        <Modal.Header closeButton>
                            <Modal.Title>
                                <h3 style={{"font-weight": "bold"}}>{this.props.source.toUpperCase()}</h3>
                                <p>{this.props.title}</p>
                            </Modal.Title>
                        </Modal.Header>
                        <Modal.Body className="modal-body">
                            <p>Share via</p>
                            <Container>
                                <Row lg={3} sm={3} xs={3}>
                                    <Col>
                                        <FacebookShareButton url={this.props.url} hashtag={"#CSCI_571_NewsApp"} >
                                            <FacebookIcon round={true} />
                                        </FacebookShareButton>
                                    </Col>
                                    <Col>
                                        <TwitterShareButton url={this.props.url} hashtags={["CSCI_571_NewsApp"]} >
                                            <TwitterIcon round={true} />
                                        </TwitterShareButton>
                                    </Col>
                                    <Col>
                                        <EmailShareButton url={this.props.url} subject={"#CSCI_571_NewsApp"} >
                                            <EmailIcon round={true} />
                                        </EmailShareButton>
                                    </Col>
                                </Row>
                            </Container>
                        </Modal.Body>
                    </Modal>
                </div>
            </div>
        )
    }
}