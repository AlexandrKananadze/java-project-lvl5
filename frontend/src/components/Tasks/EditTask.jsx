// @ts-check

import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { Form, Button } from 'react-bootstrap';
import { useFormik } from 'formik';
import { useParams, useHistory } from 'react-router-dom';
import * as yup from 'yup';
import axios from 'axios';

import { actions as tasksActions } from '../../slices/tasksSlice.js';
import handleError from '../../utils.js';
import routes from '../../routes.js';
import { useAuth, useNotify } from '../../hooks/index.js';

import getLogger from '../../lib/logger.js';

const log = getLogger('edit task');
log.enabled = true;

const getValidationSchema = () => yup.object().shape({});

const EditTask = () => {
  const { t } = useTranslation();
  const executors = useSelector((state) => state.users?.users);
  const labels = useSelector((state) => state.labels?.labels);
  const taskStatuses = useSelector((state) => state.taskStatuses?.taskStatuses);

  const [task, setTask] = useState(null);
  const params = useParams();
  const auth = useAuth();
  const notify = useNotify();
  const history = useHistory();
  const dispatch = useDispatch();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const { data: currentTaskData } = await axios.get(`${routes.apiTasks()}/${params.taskId}`, { headers: auth.getAuthHeader() });
        setTask(currentTaskData);
      } catch (e) {
        handleError(e, notify, history);
      }
    };
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const f = useFormik({
    enableReinitialize: true,
    initialValues: {
      name: task?.name,
      description: task?.description,
      status: task?.taskStatus?.id,
      executor: task?.executor?.id,
      labels: task?.labels?.map(({ id }) => id),
    },
    validationSchema: getValidationSchema(),
    onSubmit: async (currentTaskData, { setSubmitting, setErrors }) => {
      try {
        const requestTask = {
          name: currentTaskData.name,
          description: currentTaskData.description,
          executorId: parseInt(currentTaskData.executor, 10),
          taskStatusId: parseInt(currentTaskData.status, 10),
          labelIds: currentTaskData.labels.map((id) => parseInt(id, 10)),
        };
        const { data } = await axios.put(`${routes.apiTasks()}/${task.id}`, requestTask, { headers: auth.getAuthHeader() });
        log('task.edit', data);
        dispatch(tasksActions.updateTask(data));
        const from = { pathname: routes.tasksPagePath() };
        history.push(from, { message: 'taskEdited' });
      } catch (e) {
        log('task.edit.error', e);
        setSubmitting(false);
        handleError(e, notify, history, auth);
        if (e.response?.status === 422 && Array.isArray(e.response?.data)) {
          const errors = e.response?.data
            .reduce((acc, err) => ({ ...acc, [err.field]: err.defaultMessage }), {});
          setErrors(errors);
        }
      }
    },
    validateOnBlur: false,
    validateOnChange: false,
  });

  if (!executors || !labels || !taskStatuses || !task) {
    return null;
  }

  return (
    <>
      <h1 className="my-4">{t('editTask')}</h1>
      <Form onSubmit={f.handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label htmlFor="name">{t('naming')}</Form.Label>
          <Form.Control
            type="text"
            value={f.values.name}
            disabled={f.isSubmitting}
            onChange={f.handleChange}
            onBlur={f.handleBlur}
            isInvalid={f.errors.name && f.touched.name}
            id="name"
            name="name"
          />
          <Form.Control.Feedback type="invalid">
            {t(f.errors.name)}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="description">{t('description')}</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            value={f.values.description}
            disabled={f.isSubmitting}
            onChange={f.handleChange}
            onBlur={f.handleBlur}
            isInvalid={f.errors.description && f.touched.description}
            id="description"
            name="description"
          />
          <Form.Control.Feedback type="invalid">
            {t(f.errors.description)}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="status">{t('status')}</Form.Label>
          <Form.Select
            nullable
            value={f.values.status}
            disabled={f.isSubmitting}
            onChange={f.handleChange}
            onBlur={f.handleBlur}
            isInvalid={f.errors.status && f.touched.status}
            id="status"
            name="status"
          >
            <option value="">{null}</option>
            {taskStatuses
              .map((status) => <option key={status.id} value={status.id}>{status.name}</option>)}
          </Form.Select>
          <Form.Control.Feedback type="invalid">
            {t(f.errors.status)}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="executor">{t('executor')}</Form.Label>
          <Form.Select
            value={f.values.executor}
            disabled={f.isSubmitting}
            onChange={f.handleChange}
            onBlur={f.handleBlur}
            isInvalid={f.errors.executor && f.touched.executor}
            id="executor"
            name="executor"
          >
            <option value="">{null}</option>
            {executors
              .map((executor) => <option key={executor.id} value={executor.id}>{`${executor.firstName} ${executor.lastName}`}</option>)}
          </Form.Select>
          <Form.Control.Feedback type="invalid">
            {t(f.errors.executor)}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="labels">{t('labels')}</Form.Label>
          <Form.Select
            multiple
            value={f.values.labels}
            disabled={f.isSubmitting}
            onChange={f.handleChange}
            onBlur={f.handleBlur}
            isInvalid={f.errors.labels && f.touched.labels}
            id="labels"
            name="labels"
          >
            {labels.map((label) => <option key={label.id} value={label.id}>{label.name}</option>)}
          </Form.Select>
          <Form.Control.Feedback type="invalid">
            {t(f.errors.labels)}
          </Form.Control.Feedback>
        </Form.Group>

        <Button variant="primary" type="submit">
          {t('edit')}
        </Button>
      </Form>
    </>
  );
};

export default EditTask;
