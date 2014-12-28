#! /usr/bin/env python
#-*-coding=utf-8-*-

import os, sys

class GetLines():
	"""docstring for GetLines"""
	def __init__(self, dir):
		self.dir = dir
		self.filelist = []
		self.importStr = "import"
		self.singleCommandStr = "//"
		self.multiCommandStr = "/*"
		self.multiCommandStr2 = "/**"
		self.multiCommandStr3 = "*/"
		self.postfix = '.java'
		self.RecordedName = {}
		self.RecordedName['filepath'] = 'filepath'
		self.RecordedName['singleTotalLines'] = 'singleTotalLines'
		self.RecordedName['singleImportLines'] = 'singleImportLines'
		self.RecordedName['singleWhiteLines'] = 'singleWhiteLines'
		self.RecordedName['singleCommandLines'] = 'singleCommandLines'
		self.RecordedName['multiCommandLines'] = 'multiCommandLines'

	def getFileList(self, filelist,  parentDir):
		for fl in os.listdir(parentDir):
			subDir = parentDir + os.sep + fl
			if os.path.isfile(subDir) and subDir.endswith(self.postfix):
				filelist.append(subDir)
			else:
				self.getFileList(filelist, subDir)

	def getFiles(self, srcDir):
		filelist = []
		self.getFileList(filelist, srcDir)

		filelist.sort()
		print 'total files:', len(filelist)
		return filelist


	def getLinesOfOneFile(self, filepath):
		singleTotalLines = 0
		singleWhiteLines = 0
		singleCommandLines = 0;
		multiCommandLines = 0;
		singleImportLines = 0

		hF = open(filepath, 'r')
		lines = hF.readlines()
		singleTotalLines = len(lines)
		hF.close()

		multiCommandCounter = 0
		isMultiCommand = False

		for line in lines:
			line = line.strip()
			if not line:
				singleWhiteLines = singleWhiteLines + 1
				continue
			elif not isMultiCommand and line.startswith(self.importStr):
				singleImportLines = singleImportLines + 1
			elif not isMultiCommand and line.startswith(self.singleCommandStr):
				singleCommandLines = singleCommandLines + 1
			elif not isMultiCommand and (line.startswith(self.multiCommandStr) \
						or line.startswith(self.multiCommandStr2)):
				isMultiCommand = True
				multiCommandCounter = 0
			elif isMultiCommand and line.startswith(self.multiCommandStr3):
				isMultiCommand = False
				multiCommandLines = multiCommandLines + multiCommandCounter + 2
			elif isMultiCommand:
				multiCommandCounter = multiCommandCounter + 1

		singleLineInfo = {}
		singleLineInfo['filepath'] = filepath
		singleLineInfo['singleTotalLines'] = singleTotalLines
		singleLineInfo['singleImportLines'] = singleImportLines
		singleLineInfo['singleWhiteLines'] = singleWhiteLines
		singleLineInfo['singleCommandLines'] = singleCommandLines
		singleLineInfo['multiCommandLines'] = multiCommandLines

		singleLineInfo2 = [filepath, singleTotalLines, singleImportLines, \
					singleWhiteLines, singleCommandLines, multiCommandLines]
		
		return singleLineInfo, singleLineInfo2

	def getLinesInfo(self, filelist):
		linesInfos = []
		linesInfos2 = []
		for fl in filelist:
			singleLineInfo, singleLineInfo2 = self.getLinesOfOneFile(fl)
			linesInfos.append(singleLineInfo);
			linesInfos2.append(singleLineInfo2);
		return linesInfos, linesInfos2

	def getsingleTotalLinesInfo(self, linesInfo):
		for li in linesInfo:
			keys = li.keys()
			keys.sort()
			for k in keys:
				print k, li[k]
			print '**********************'

	def getsingleTotalLinesInfo2(self, linesInfo2):
		t_singleTotalLines = 0
		t_singleImportLines = 0
		t_singleWhiteLines = 0
		t_singleCommandLines = 0
		t_multiCommandLines = 0

		for li in linesInfo2:
			# for k in li:
			# 	print  k
			# print '**********************'
			filepath, singleTotalLines, singleImportLines, singleWhiteLines, \
						singleCommandLines, multiCommandLines = li
			t_singleTotalLines = t_singleTotalLines + singleTotalLines
			t_singleImportLines = t_singleImportLines + singleImportLines
			t_singleWhiteLines = t_singleWhiteLines + singleWhiteLines
			t_singleCommandLines = t_singleCommandLines + singleCommandLines
			t_multiCommandLines = t_multiCommandLines + multiCommandLines

		realTotalLines = t_singleTotalLines - t_singleWhiteLines \
					- t_multiCommandLines - t_singleCommandLines
		print "real total lines: ", realTotalLines
		print "total lines: ", t_singleTotalLines
		print "total import lines: ", t_singleImportLines
		print "total white lines: ", t_singleWhiteLines
		print "total single command lines: ", t_singleCommandLines
		print "total multi-command lines: ", t_multiCommandLines

	def run(self, srcDir):
		filelist = self.getFiles(srcDir)
		linesInfos, linesInfos2= self.getLinesInfo(filelist)
		self.getsingleTotalLinesInfo(linesInfos)
		self.getsingleTotalLinesInfo2(linesInfos2)

if __name__ == '__main__':
	srcDir = "src"
	GL = GetLines(srcDir)
	GL.run(srcDir)