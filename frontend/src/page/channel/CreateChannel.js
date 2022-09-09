import React, {useState, useEffect, useContext, useLayoutEffect} from 'react';
import { AuthContext } from '../../App';
import ChannelService from '../../api/service/ChannelService'
import {Container, Col, Row, Form, Button, Card, FloatingLabel} from 'react-bootstrap'
import {createChannelValidation as validation} from "../../api/validation";
import $ from "jquery";

const CreateChannel = () => {
    const context = useContext(AuthContext);
    const [thumbnail, setThumbnail] = useState(null)

    const handleChangeThumbnail = (event) => {
        setThumbnail(event.target.files[0])
    }

    const handleCreateButton = async () => {
        let formData = new FormData(document.getElementById('form'));
        let thumbnailImageDir = await ChannelService.uploadThumbnail(thumbnail)

        let channel = {
            memberEmail : context.member.id,
            name : formData.get('name'),
            limitedMemberNumber : formData.get('limitedMemberNumber'),
            description : $('#summernote').val(),
            category : formData.get('category'),
            thumbnailDescription : formData.get('thumbnailDescription'),
            thumbnailImage : thumbnailImageDir,
        }
        if (validate(channel)) {
            await ChannelService.create(channel);
        }
    }

    function getUploadFiles(htmlCode) {
        let uploadFiles = []
        let offset = 0

        while (htmlCode.indexOf('img src', offset) !== -1) {
            let uploadFile = {}

            let imgTagStr = htmlCode.substr(htmlCode.indexOf('img src', offset))
            let firstIdx = imgTagStr.indexOf('"') + 1
            let lastIdx = imgTagStr.indexOf('"', firstIdx)

            uploadFile.storeFileName = imgTagStr.substring(firstIdx, lastIdx)
            uploadFile.uploadFileName = 'image'

            uploadFiles.push(uploadFile)

            offset = htmlCode.indexOf('img src', offset) + 'img src'.length
        }
        return uploadFiles;
    }

    const validate = (channel) => {
        let valid = true;

        removeAlertMassageBox()
        if (!validation.name.test(channel.name) || channel.name == null) {
            $('#alert').append('<h5>[프로젝트 이름] : 이름은 2자리 이상 20이하 자리수만 입력 가능합니다.</h5>')
            valid = false;
        }
        if (!validation.limitedMemberNumber.test(channel.limitedMemberNumber) || channel.limitedMemberNumber == null) {
            $('#alert').append('<h5>[프로젝트 인원] : 숫자만 입력가능하며 일의자리수부터 백의자리수까지 입력 가능합니다.</h5>')
            valid = false;
        }
        if (!validation.thumbnailDescription.test(channel.thumbnailDescription) || channel.thumbnailDescription == null) {
            $('#alert').append('<h5>[프로젝트 썸네일 인사말] : 1자리 이상 30이하 자리수만 입력 가능합니다.</h5>')
            valid = false;
        }
        if (channel.category === 'NONE') {
            $('#alert').append('<h5>[카테고리] : 카테고리를 정해주세요.</h5>')
            valid = false;
        }

        if (!valid) {
            showAlertMassageBox()
        }
        return valid
    }

    const removeAlertMassageBox = () => {
        $('#alert').children('h5').remove();
    }

    const showAlertMassageBox = () => {
        $('#alert').css('display', 'block')
    }

    const hideAlertMassageBox = () => {
        $('#alert').css('display', 'none')
    }

    const handleBackButton = () => {
        window.history.back()
    }

    function configSummernote() {
        $(document).ready(function() {
            $('#summernote').summernote({
                height: 400,
                minHeight: null,
                maxHeight: null,
                callbacks: {
                    onImageUpload : function(images) {
                        for (let i = 0; i < images.length; i++) {
                            let reader = new FileReader();
                            reader.readAsDataURL(images[i])

                            reader.onloadend = () => {
                                const base64 = reader.result
                                $('#summernote').summernote('insertImage', base64)
                            }
                        }
                    },
                    onPaste: function (e) {
                        let clipboardData = e.originalEvent.clipboardData;
                        if (clipboardData && clipboardData.items && clipboardData.items.length) {
                            let item = clipboardData.items[0];
                            if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
                                e.preventDefault();
                            }
                        }
                    }
                }
            })
        })
    }

    useEffect(() => {
        hideAlertMassageBox()
    }, [])

    useLayoutEffect(() => {
        configSummernote()
    }, [])


    return (
        <>
            <Container style={{width: "100%", fontFamily: "sans-serif"}}>
                <Row className='d-flex justify-content-center align-items-center'>
                    <Form id='form' style={{width: "70%"}}>
                        <FloatingLabel label="채널 정원" className="mb-3 " style={{width: "11%", display: 'inline-block', marginRight: 20}}>
                            <Form.Control defaultValue='1' type="number" id="limitedMemberNumber" name="limitedMemberNumber" placeholder="회원 제한 수"/>
                        </FloatingLabel>

                        <Form.Select className="mb-3 form-control" name="category" id="category"
                                     style={{width: "86%", display: 'inline-block'}}>
                            <option className="fs-4" selected value="NONE" placeholder="name@example.com">카테고리를 선택해주세요</option>
                            <option className="fs-4" value="STUDY">스터디</option>
                            <option className="fs-4" value="PROJECT">프로젝트</option>
                        </Form.Select>

                        <FloatingLabel label="스터디 이름" className="mb-3" style={{width: "100%"}}>
                            <Form.Control id="name" name="name" />
                        </FloatingLabel>

                        <FloatingLabel label="스터디 소개" className="mb-3" style={{width: "100%"}}>
                            <Form.Control id="thumbnailDescription" name="thumbnailDescription" />
                        </FloatingLabel>

                        <Form.Group style={{width: "100%"}}>
                            <Form.Label>대표 사진</Form.Label>
                            <Form.Control onChange={handleChangeThumbnail} id='file' type='file' />
                        </Form.Group>

                        <Form.Group className="mb-3 mt-5" style={{width: "100%"}}>
                            <Form.Label className="fs-3 fw-bold">스터디 설명</Form.Label>
                            <textarea id='summernote' />
                        </Form.Group>

                        <div className="alert alert-danger mt-5" id="alert" role="alert">
                            <h4 className="alert-heading">입력한 정보에 문제가 있네요!</h4>
                            <hr/>
                        </div>

                        <Row className="row mt-5">
                            <Col className="col">
                                <button onClick={handleCreateButton} className="w-100 btn btn-primary btn-lg" type="button" id="createButton">
                                    생성
                                </button>
                            </Col>
                            <Col className="col">
                                <button onClick={handleBackButton} className="w-100 btn btn-secondary btn-lg" type="button" id="cancel">
                                    뒤로가기
                                </button>
                            </Col>
                        </Row>
                    </Form>
                </Row>
            </Container>
        </>
    );
};

export default CreateChannel;
