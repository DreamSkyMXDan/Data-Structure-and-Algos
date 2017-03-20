//
//  Huffman.swift
//
//  Created by daniel shen on 12/2/14.
//  Copyright (c) 2014 daniel shen. All rights reserved.
//

import Foundation

extension Character {
    func utf8Value() -> UInt8 {
        for s in String(self).utf8 {
            return s
        }
        return 0
    }
    
    func utf16Value() -> UInt16 {
        for s in String(self).utf16 {
            return s
        }
        return 0
    }
    
    func unicodeValue() -> UInt32 {
        for s in String(self).unicodeScalars {
            return s.value
        }
        return 0
    }
}

public final class Huffman {
    
    // ----------------------------- static variables --------------------
    fileprivate let CODESIZE:Int = 256
    fileprivate let ZERO:Character = "0"
    fileprivate let ONE:Character = "1"
    fileprivate let BSPACE:Character = " "
    fileprivate let EMPTY_STRING:String = ""
    // struct doesn't allow optional property type
    struct node {
        var left:Int = 0
        var right:Int = 0
    }
    
    // private variables
    
    var _transTb:[String]?
    var _treeInt:[Int] = [33,36,37,39,43,63,74,90,91,93,94,95,
        96,123,125,126,-1,-2,41,-3,-4,-5,-6,-7,-8,38,40,88,122,
        -9,-10,-11,-12,9,-13,81,75,-14,-15,59,-16,-17,-18,86,-19,
        -20,71,119,-21,64,113,107,-22,35,102,-23,-24,72,42,66,-25,
        89,-26,79,106,-27,-28,120,44,-29,-30,85,82,-31,98,87,-32,
        73,-33,10,13,46,-34,65,77,99,-35,-36,69,-37,-38,76,45,-39,
        57,67,-40,80,-41,58,55,53,78,54,-42,-43,56,-44,-45,104,52,
        -46,118,51,68,-47,-48,117,-49,112,121,-50,83,115,92,50,-51,
        47,-52,84,-53,111,-54,103,49,70,109,-55,-56,-57,-58,-59,-60,
        -61,60,62,-62,48,-63,114,-64,100,-65,61,-66,-67,108,-68,105,
        -69,-70,110,116,97,-71,-72,-73,-74,-75,-76,-77,34,32,-78,-79,
        -80,-81,101,-82,-83,-84,-85,-86,-87,-88,-89,-90,-91,-92,-93,
        -94,-95]
    
    
    init() {
        buildTree();
    }
    
    //private functions
    fileprivate func buildTree() {
        var tree = Array<node>(repeating: node(), count: CODESIZE*2)
        
        tree[0] = node(left: 0,right: 0)
        
        let len:Int = _treeInt.count / 2
        
        for i in 1...len {
            tree[i] = node()
            tree[i].left = _treeInt[2 * i - 2]
            tree[i].right = _treeInt[2 * i - 1]
        }
        _transTb = [String](repeating: EMPTY_STRING, count: CODESIZE)
        buildTransTable(tree, i: len, code: EMPTY_STRING)
        tree = Array<node>()
    }
    
    // Builds a translation table from a given huffman tree.
    fileprivate func buildTransTable(_ tree:[node], i:Int, code:String) {
        let left:Int = tree[i].left
        let right:Int = tree[i].right
        
        if left >= 0 {
            _transTb![left] = code + String(ZERO)
        } else {
            buildTransTable(tree, i: abs(left), code: code + String(ZERO))
        }
        
        if right >= 0 {
            _transTb![right] = code + String(ONE)
        } else {
            buildTransTable(tree, i: abs(right), code: code + String(ONE))
        }
    }
    
    // translate to byte
    fileprivate func transToByte(_ b:UInt8)->String {
        let bspace:UInt8 = BSPACE.utf8Value()  // convert char to byte
        var bRet:String? = _transTb![Int(bspace)]
        let len:Int = _transTb!.count
        // first out of bounce
        if Int(b) > len - 1 {
            bRet = _transTb![Int(bspace)]
        } else {
            bRet = _transTb![Int(b)]
            if bRet == nil {
                bRet = _transTb![Int(bspace)]
            }
        }

        return bRet!
    }
    
    // public functions
    // ----------------------------- interface ---------------------------
    // compress
    func compress(_ datain:[UInt8], dataout:inout [UInt8])->Int {
        
        var idx:Int = 0
        var odx:Int = 0
        var s:String = EMPTY_STRING
        var sr:String = EMPTY_STRING
        let ilen:Int = datain.count
        var _:Int
        
        while sr != EMPTY_STRING || idx < ilen {
            s = sr
            if s == EMPTY_STRING {
                s = transToByte(datain[idx])
                idx += 1
            } else {
                if s.characters.count < 8 {
                    if idx == ilen {
                        for _ in 0...7 - s.characters.count {
                            s += String(ZERO)
                        }
                    } else {
                        s += transToByte(datain[idx]);
                        idx += 1
                    }
                }
            }
            
            if s.characters.count > 7 {
                for i:UInt8 in 0...7 {
                    if Array(s.characters)[Int(i)] == ONE {
                        dataout[odx] = (UInt8)(dataout[odx] | (0x01 << (7 - i)))
                    }
                }
                odx += 1
                sr = s.substring(from: s.characters.index(s.startIndex, offsetBy: 8))
            } else {
                sr = s
            }
        }
        
        return odx
    }
    
    // uncompresses
    func uncompress(_ datain:[UInt8])->[UInt8] {
        var idx:Int = 0
        var odx :Int = 0
        var dataout:[UInt8]? = nil
        var temp:[UInt8]? = nil
        
        let ilen = datain.count
        var it = _treeInt.count - 2
        temp = [UInt8](repeating: 0x00, count: 2 * ilen + CODESIZE)
        
        while idx < ilen {
            
            for i in 0...7 {
                
                let b:Int = Int((datain[idx] >> UInt8(7-i)) & 0x01)
                if _treeInt[it + b] >= 0 {
                    if odx >= temp!.count {
                        var newArray:[UInt8] = [UInt8](repeating: 0x00, count: temp!.count + CODESIZE)
                        newArray.replaceSubrange(0...temp!.count-1, with: temp!)  // array copy
                        temp = newArray
                    }
                    temp![odx] = UInt8(_treeInt[it + b])
                    it = _treeInt.count - 2
                    odx += 1
                } else {
                    it = (-2) * _treeInt[it + b] - 2
                }
            }
            idx += 1
        }
        
        dataout = [UInt8](repeating: 0x00, count: odx)
        dataout!.replaceSubrange(0...odx-1, with: temp!)
        
        return dataout!
    }

}
