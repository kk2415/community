import React, {useState, useEffect, useContext, useLayoutEffect} from 'react';
import { AuthContext } from '../../App';
import ChannelService from '../../api/ChannelService'
import {Container, Col, Row, Form, Button, Card} from 'react-bootstrap'
import {uploadFile} from "../../api/FileService";
import $ from "jquery";
import SummerNote from '../../component/SummerNote'
import RichTextEditor from '../../component/SummerNote'

const CreateChannel = () => {
    const getChannelId = () => {
        let pathname = decodeURI($(window.location).attr('pathname'))

        return Number(pathname.substr(pathname.lastIndexOf('/') + 1))
    }

    const loadDescription = async (channelId) => {
        let result = await ChannelService.getDescription(channelId)

        console.log(result)
        setDescription(result)
        console.log(description)
    }

    const handleChangeThumbnail = (event) => {
        setThumbnail(event.target.files[0])
    }

    const handleModifyButton = async () => {
        let formData = new FormData(document.getElementById('form'));
        let thumbnailImageDir = description.thumbnailImage

        if (thumbnail !== null) {
            thumbnailImageDir = await uploadFile('/api/member/image', 'POST', thumbnail)
        }

        // let uploadFiles = getUploadFiles(contents);

        let channel = {
            name : $('#name').val(),
            limitedMemberNumber : $('#limitedMemberNumber').val(),
            description : contents,
            category : $('#category').val(),
            thumbnailDescription : $('#thumbnailDescription').val(),
            thumbnailImage : thumbnailImageDir,
            // uploadFiles : uploadFiles,
        }
        console.log(channel)

        await ChannelService.modify(channel, channelId)
    }

    function getUploadFiles(htmlCode) {
        let uploadFiles = []
        let offset = 0

        while (htmlCode.indexOf('img src', offset) !== -1) {
            let uploadFile = {}

            let imgTagStr = htmlCode.substr(htmlCode.indexOf('img src', offset))
            let firstIdx = imgTagStr.indexOf('"') + 1
            let lastIdx = imgTagStr.indexOf('"', firstIdx)

            let base64 = imgTagStr.substring(firstIdx, lastIdx)
            uploadFile.storeFileName = base64.substr(base64.indexOf(',') + 1)
            uploadFile.uploadFileName = base64

            uploadFiles.push(uploadFile)

            offset = htmlCode.indexOf('img src', offset) + 'img src'.length
        }
        return uploadFiles;
    }

    const handleName = () => {

    }

    const context = useContext(AuthContext);
    const [thumbnail, setThumbnail] = useState(null)
    const [contents, setContents] = useState(null)
    const [onLoading, setOnLoading] = useState(true)
    const [channelId, setChannelId] = useState(getChannelId())
    const [description, setDescription] = useState(null)

    const loadContents = (data) => {
        setContents(data)
    }

    const uploadImage = (images, insertImage) => {
        for (let i = 0; i < images.length; i++) {
            uploadFile('/api/channel/descriptionFiles', 'POST', images[i])
                .then((data) => {
                    console.log(data)
                })
                .catch((error) => {
                    console.log(error)
                })
            // uploadImage(images[i], insertImage);
        }
    }

    const showChannel = () => {
        // let e = $.Event('keypress')
        // e.which = 13;

        if (description) {
            $('#name').val(description.name)
            $('#limitedMemberNumber').val(description.limitedMemberNumber)
            $('#thumbnailDescription').val(description.thumbnailDescription)
        }
    }

    useEffect(() => {
        showChannel()
    }, [description])

    useLayoutEffect(() => {
        setChannelId(getChannelId())
        loadDescription(channelId)
    }, [])

    return (
        <>
            <Container>
                {
                    description &&
                    <Form id='form'>
                        <Form.Group className='mb-4'>
                            <Form.Label>카테고리</Form.Label>
                            <Form.Select value={description.category} aria-label="Default select example" name="category" id="category">
                                <option selected value="NONE">카테고리를 선택해주세요</option>
                                <option value="STUDY">스터디</option>
                                <option value="PROJECT">프로젝트</option>
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>이름</Form.Label>
                            <input className="form-control" id="name" type="text" placeholder="Enter channel name..."
                                   maxLength="20" data-sb-validations="required"/>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>회원 제한 수</Form.Label>
                            <input className="form-control" id="limitedMemberNumber" type="text"
                                   placeholder="Enter channel limited number..."
                                   maxLength="3" data-sb-validations="required"/>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>설명</Form.Label>
                            <RichTextEditor setContents={loadContents} contents={description.description} />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>썸네일 인사말</Form.Label>
                            <input className="form-control" id="thumbnailDescription" type="text"
                                   placeholder="짤막한 소개글 작성해주세요!"
                                   maxLength="30"/>
                        </Form.Group>

                        <Form.Group>
                            <Form.Label>대표 사진</Form.Label>
                            <Form.Control onChange={handleChangeThumbnail} id='file' type='file' />
                        </Form.Group>

                        <div className="row mt-5">
                            <div className="col">
                                <button onClick={handleModifyButton} className="w-100 btn btn-primary btn-lg" type="button"
                                        id="modifyButton">수정</button>
                            </div>
                            <div className="col">
                                <button className="w-100 btn btn-secondary btn-lg" type="button" id="cancel">뒤로가기</button>
                            </div>
                        </div>
                    </Form>
                }
            </Container>
        </>
    );
};

export default CreateChannel;